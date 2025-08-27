package com.cjproductions.vicinity.discover.data.paging.events

import Vicinity.discover.data.BuildConfig
import app.cash.paging.ExperimentalPagingApi
import app.cash.paging.LoadType
import app.cash.paging.PagingState
import app.cash.paging.RemoteMediator
import app.cash.paging.RemoteMediatorMediatorResult
import app.cash.paging.RemoteMediatorMediatorResultError
import app.cash.paging.RemoteMediatorMediatorResultSuccess
import com.cjproductions.vicinity.core.data.database.VicinityDatabase
import com.cjproductions.vicinity.discover.data.local.DefaultLocalDiscoverDataSource.Companion.NEXT_PAGE_KEY
import com.cjproductions.vicinity.discover.data.local.LocalDiscoverDataSource
import com.cjproductions.vicinity.discover.data.local.model.EventDataEntity
import com.cjproductions.vicinity.discover.data.remote.RemoteDiscoverDataSource
import com.cjproductions.vicinity.discover.domain.model.Event
import com.cjproductions.vicinity.location.data.latLongToHash
import com.cjproductions.vicinity.location.domain.model.UserLocation
import com.cjproductions.vicinity.support.search.DateType.AnyDate
import com.cjproductions.vicinity.support.search.DateType.Custom
import com.cjproductions.vicinity.support.search.DateType.ThisWeekend
import com.cjproductions.vicinity.support.search.DateType.Today
import com.cjproductions.vicinity.support.search.DateType.Tomorrow
import com.cjproductions.vicinity.support.search.FilterParams
import com.cjproductions.vicinity.support.search.SearchParams
import com.cjproductions.vicinity.support.search.SortType
import com.cjproductions.vicinity.support.search.SortingOption.Date
import com.cjproductions.vicinity.support.search.SortingOption.Name
import com.cjproductions.vicinity.support.search.SortingOption.Relevance
import com.cjproductions.vicinity.support.tools.getCurrentTime
import com.cjproductions.vicinity.support.tools.getDateTimeString
import com.cjproductions.vicinity.support.tools.toDouble
import comcjproductionsvicinitycoredatadatabase.EventEntity
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.coroutines.cancellation.CancellationException

@OptIn(ExperimentalPagingApi::class)
class EventsRemoteMediator(
  private val searchParams: SearchParams?,
  private val filterParams: FilterParams?,
  private val location: UserLocation?,
  private val remoteDiscoverDataSource: RemoteDiscoverDataSource,
  private val localDiscoverDataSource: LocalDiscoverDataSource,
  private val database: VicinityDatabase,
): RemoteMediator<Int, EventEntity>() {

  override suspend fun load(
    loadType: LoadType,
    state: PagingState<Int, EventEntity>,
  ): RemoteMediatorMediatorResult {

    return try {
      val pageNo = when (loadType) {
        LoadType.REFRESH -> {
          localDiscoverDataSource.resetEventsTable()
          0
        }

        LoadType.PREPEND -> return RemoteMediatorMediatorResultSuccess(endOfPaginationReached = true)

        LoadType.APPEND -> localDiscoverDataSource.getNextPageEventKey()
          ?: return MediatorResult.Success(endOfPaginationReached = true)
      }

      val query = getEventQuery(
        searchParams = searchParams,
        filterParams = filterParams,
        page = pageNo,
        userLocation = location,
      )

      val discoverFeed = remoteDiscoverDataSource.searchDiscoverFeed(query)

      if (discoverFeed.isFailure) {
        return RemoteMediatorMediatorResultError(Throwable(UNKNOWN_ERROR))
      }

      val nextPageNumber = (pageNo + 1).takeUnless {
        pageNo >= (discoverFeed.getOrNull()?.page?.totalPages ?: 0).dec()
      }

      localDiscoverDataSource.insertEventRemoteKey(
        id = NEXT_PAGE_KEY,
        nextPageKey = nextPageNumber?.toLong(),
      )

      discoverFeed.getOrNull()?.let { feed ->
        database.vicinityDbQueries.transaction {
          feed.eventsEmbedded.events.mapIndexed { index, event ->
            event.getEventData { title, category ->
              localDiscoverDataSource.getEvent(
                title = title,
                category = category,
              )
            }.let { event -> localDiscoverDataSource.insertEvent(event) }
          }
        }
      }

      MediatorResult.Success(
        endOfPaginationReached = (discoverFeed.getOrNull()?.page?.number ?: 0) >=
            (discoverFeed.getOrNull()?.page?.totalPages ?: 0).dec()
      )

    } catch (e: Exception) {
      if (e is CancellationException) throw e
      RemoteMediatorMediatorResultError(Throwable(UNKNOWN_ERROR))
    }
  }

  private fun getEventQuery(
    searchParams: SearchParams?,
    filterParams: FilterParams?,
    page: Int,
    userLocation: UserLocation?,
  ): Map<String, Any?> {
    val timeZone = TimeZone.currentSystemDefault()
    return buildMap {
      put(
        key = API_KEY,
        value = BuildConfig.API_KEY,
      )
      put(
        key = SIZE,
        value = PAGE_SIZE,
      )
      put(
        key = PAGE,
        value = page,
      )
      searchParams?.let { searchParams -> addSearchParams(searchParams) } ?: run {
        addLocationToQuery(userLocation)
      }

      addFilterCriteria(
        filter = filterParams,
        timeZone = timeZone,
      )
    }
  }

  private fun QueryMap.addLocationToQuery(userLocation: UserLocation?) =
    userLocation?.let { location ->
      if (location.isUserSet) {
        put(
          key = COUNTRY_CODE,
          value = location.userSetLocation?.countryCode,
        )
      } else {
        location.defaultLocation?.let { defaultLocation ->
          with(defaultLocation) {
            put(
              key = GEO_POINT,
              value = convertLatLongToGeoHash(latitude, longitude),
            )
          }
        }
      }
    }

  private fun QueryMap.addFilterCriteria(
    filter: FilterParams?,
    timeZone: TimeZone,
  ) {
    val currentTime = Clock.System.now().toLocalDateTime(timeZone = timeZone)
    if (filter == null) {
      val formattedDateTime = currentTime.getDateTimeString().substringBefore(".") + 'Z'
      put(
        key = START_DATE_TIME,
        value = formattedDateTime,
      )
      return
    }
    filter.sortType?.let { sortType ->
      val sortOrder = sortType.toSortOrder()
      when (sortType.sortingOption) {
        Relevance -> {
          put(
            key = SORT,
            value = "$RELEVANCE,$sortOrder",
          )
        }

        Date -> {
          put(
            key = SORT,
            value = "$DATE,$sortOrder",
          )
        }

        Name -> {
          put(
            key = SORT,
            value = "$NAME,$sortOrder",
          )
        }
      }
    }

    when (filter.dateType) {
      Today -> {
        val startDateInstant = currentTime.date.atStartOfDayIn(timeZone)

        val startDateTime = startDateInstant
          .toLocalDateTime(timeZone)
          .getDateTimeString()

        val nextDayDateTime = startDateInstant
          .plus(
            value = TOTAL_DAY_HOURS,
            unit = DateTimeUnit.HOUR,
          ).toLocalDateTime(timeZone = timeZone)
          .getDateTimeString()

        put(
          key = START_END_DATE_TIME,
          value = "$startDateTime,$nextDayDateTime",
        )
      }

      Tomorrow -> {
        val startDateInstant = currentTime.date.plus(
          value = 1,
          unit = DateTimeUnit.DAY,
        ).atStartOfDayIn(timeZone)

        val startDateTime = startDateInstant
          .toLocalDateTime(timeZone)
          .getDateTimeString()
        val nextDayDateTime = startDateInstant
          .plus(
            value = TOTAL_DAY_HOURS,
            unit = DateTimeUnit.HOUR,
          ).toLocalDateTime(timeZone = timeZone)
          .getDateTimeString()

        put(
          key = START_END_DATE_TIME,
          value = "$startDateTime,$nextDayDateTime",
        )
      }

      ThisWeekend -> {
        findNearestWeekend(currentTime).let { (startDateTime, endDateTime) ->
          put(
            key = START_END_DATE_TIME,
            value = "${startDateTime.getDateTimeString()},${endDateTime.getDateTimeString()}",
          )
        }
      }

      Custom -> {
        filter.selectedDates?.takeIf { it.isNotEmpty() }?.let { dates ->
          when (dates.size) {
            1 -> {
              val nextDayDateTime = dates.first()
                .toInstant(TimeZone.currentSystemDefault())
                .plus(
                  value = TOTAL_DAY_HOURS,
                  unit = DateTimeUnit.HOUR,
                ).toLocalDateTime(timeZone = timeZone).getDateTimeString()
              val startDateTime = dates.first().getDateTimeString()
              put(
                key = START_END_DATE_TIME,
                value = "$startDateTime,$nextDayDateTime",
              )
            }

            2 -> {
              val startDateTime = dates.first().getDateTimeString()
              val endDateTime = dates.last().getDateTimeString()
              put(
                key = START_END_DATE_TIME,
                value = "$startDateTime,$endDateTime"
              )
            }
          }
        }
      }

      AnyDate -> null
    }

    filter.classificationNames?.let { names ->
      put(
        key = CLASSIFICATION_NAME,
        value = names.joinToString(SEPARATOR) { it.value },
      )
    }
  }

  private fun QueryMap.addSearchParams(searchParams: SearchParams) {
    searchParams.let { searchParams ->
      searchParams.keyword?.let { keyword ->
        put(
          key = KEYWORD,
          value = keyword,
        )
      }

      searchParams.longitude?.let { longitude ->
        with(searchParams) {
          put(
            key = GEO_POINT,
            value = convertLatLongToGeoHash(latitude!!, longitude),
          )
        }
      }
    }
  }

  private fun SortType.toSortOrder() = ASC.takeIf { isAscending } ?: DESC

  private fun findNearestWeekend(currentDateTime: LocalDateTime): Pair<LocalDateTime, LocalDateTime> {
    val currentDate = currentDateTime.date
    val currentDayOfWeek = currentDate.dayOfWeek

    val daysUntilFriday = when (currentDayOfWeek) {
      DayOfWeek.SATURDAY -> 6
      DayOfWeek.SUNDAY -> 5
      DayOfWeek.MONDAY -> 4
      DayOfWeek.TUESDAY -> 3
      DayOfWeek.WEDNESDAY -> 2
      DayOfWeek.THURSDAY -> 1
      else -> 0
    }

    val nearestFriday = currentDate.plus(daysUntilFriday, DateTimeUnit.DAY)

    val startDateTime = LocalDateTime(
      date = nearestFriday,
      time = LocalTime(0, 0, 0)
    )

    val nearestSunday = nearestFriday.plus(2, DateTimeUnit.DAY)
    val endDateTime = LocalDateTime(
      date = nearestSunday,
      time = LocalTime(23, 59, 59)
    )

    return Pair(startDateTime, endDateTime)
  }

  private fun Event.getEventData(
    getEvent: (title: String, category: String) -> EventEntity?,
  ): EventDataEntity {
    val marketIds = venue?.marketIds?.joinToString(SEPARATOR)
    var eventIds = id
    var venueIds = venue?.id
    var currentImage: String = image.orEmpty()
    var earliestStartDate: Double? = dateTimes.firstOrNull()?.toDouble()
    var latestStartDate: Double? = dateTimes.lastOrNull()?.toDouble()
    var createdAt: Double? = null
    getEvent(
      title,
      classifications.firstOrNull()?.name.orEmpty(),
    )?.let { eventEntity ->
      createdAt = eventEntity.createdAt
      eventIds =
        eventEntity.eventIds.split(SEPARATOR).plus(id).distinctAndJoin()
      venueIds = eventEntity.venueIds.split(SEPARATOR).plus(venue?.id).distinctAndJoin()
      earliestStartDate =
        dateTimes.firstOrNull()?.toDouble()?.let { dateTime ->
          if (dateTime < (eventEntity.earliestDate ?: 0.0)) dateTime
          else eventEntity.earliestDate
        }
      latestStartDate = dateTimes.lastOrNull()?.toDouble()?.let { dateTime ->
        if (dateTime > (eventEntity.latestDate ?: 0.0)) dateTime
        else eventEntity.latestDate
      }
      currentImage =
        currentImage.takeIf { eventEntity.imageUrl.isEmpty() } ?: eventEntity.imageUrl
    }
    return EventDataEntity(
      category = classifications.firstOrNull()?.name.orEmpty(),
      venueIds = venueIds.orEmpty(),
      eventIds = eventIds,
      originalTitle = title,
      imageUrl = currentImage,
      earliestDate = earliestStartDate,
      latestDate = latestStartDate,
      marketIds = marketIds.orEmpty(),
      createdAt = createdAt ?: getCurrentTime(),
    )
  }

  private fun <T> List<T>.distinctAndJoin() = distinct().joinToString(",")

  private fun convertLatLongToGeoHash(lat: Double, long: Double) =
    latLongToHash(lat = lat, long = long)

  companion object {
    private const val TOTAL_DAY_HOURS = 24
    private const val KEYWORD = "keyword"
    private const val API_KEY = "apikey"
    private const val SIZE = "size"
    private const val CLASSIFICATION_NAME = "classificationName"
    private const val START_END_DATE_TIME = "localStartDateTime"
    private const val START_DATE_TIME = "startDateTime"
    private const val GEO_POINT = "geoPoint"
    private const val COUNTRY_CODE = "countryCode"
    private const val PAGE = "page"
    private const val SORT = "sort"
    private const val RELEVANCE = "relevance"
    private const val DATE = "date"
    private const val NAME = "name"
    private const val ASC = "asc"
    private const val DESC = "desc"
    private const val UNKNOWN_ERROR = "Unknown Error"
    private const val SEPARATOR = ","
    private const val PAGE_SIZE = 20
  }
}

private typealias QueryMap = MutableMap<String, Any?>