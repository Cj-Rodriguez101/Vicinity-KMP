package com.cjproductions.vicinity.eventDetail.data.remote

import Vicinity.discover.data.BuildConfig.API_KEY
import Vicinity.discover.data.BuildConfig.BASE_URL
import com.cjproductions.vicinity.core.domain.util.DataError
import com.cjproductions.vicinity.core.domain.util.Result
import com.cjproductions.vicinity.core.domain.util.map
import com.cjproductions.vicinity.discover.data.remote.model.DiscoverEventsDto
import com.cjproductions.vicinity.discover.data.remote.model.TicketMasterEventsDto
import com.cjproductions.vicinity.discover.data.remote.model.mapToDomain
import com.cjproductions.vicinity.discover.data.remote.routes.EVENTS
import com.cjproductions.vicinity.discover.domain.model.DiscoverFeed
import com.cjproductions.vicinity.eventDetail.data.remote.model.EventDetailDto
import com.cjproductions.vicinity.eventDetail.data.remote.model.mapToDomain
import com.cjproductions.vicinity.eventDetail.data.remote.routes.EVENT_DETAIL
import com.cjproductions.vicinity.eventDetail.domain.EventDate
import com.cjproductions.vicinity.eventDetail.domain.EventDetail
import com.cjproductions.vicinity.location.data.latLongToHash
import com.cjproductions.vicinity.location.domain.model.UserLocation
import com.cjproductions.vicinity.location.domain.model.getPlaceName
import com.cjproductions.vicinity.support.tools.getDateTimeString
import com.cjproductions.vicinity.support.tools.toLocalDateTime
import com.vicinity.core.data.network.get
import io.ktor.client.HttpClient
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.plus

interface RemoteEventDetailDataSource {
  suspend fun getEventDetail(id: String): Result<EventDetail, DataError.Network>
  suspend fun getEventsBasedOnKeyword(
    keyword: String,
    location: UserLocation?,
  ): Result<DiscoverFeed, DataError.Network>

  suspend fun getEventsBasedOnIdsAndDate(
    ids: List<String>,
    date: Double,
  ): Result<List<EventDate>, DataError.Network>
}

class DefaultRemoteEventDetailDataSource(
  private val httpClient: HttpClient,
): RemoteEventDetailDataSource {
  override suspend fun getEventDetail(id: String): Result<EventDetail, DataError.Network> {
    return httpClient.get<EventDetailDto>(
      route = "$EVENT_DETAIL/$id.json",
      baseUrl = BASE_URL,
      queryParameters = buildMap { put(EVENT_API_KEY, API_KEY) }
    ).map { eventDto -> eventDto.mapToDomain() }
  }

  override suspend fun getEventsBasedOnKeyword(
    keyword: String,
    location: UserLocation?,
  ): Result<DiscoverFeed, DataError.Network> {
    return httpClient.get<TicketMasterEventsDto>(
      route = EVENTS,
      baseUrl = BASE_URL,
      queryParameters = buildMap {
        put(EVENT_API_KEY, API_KEY)
        put(KEYWORD, keyword)
        put(SIZE, EVENT_SEARCH_SIZE)
        location?.let { latLng ->
          if (latLng.isUserSet) {
            put(
              key = PREFERRED_COUNTRY,
              value = latLng.userSetLocation?.getPlaceName()
            )
          } else {
            latLng.defaultLocation?.let { location ->
              put(
                key = GEO_POINT,
                value = latLongToHash(lat = location.latitude, long = location.longitude)
              )
            }
          }
        }
      }
    ).map { discoverDto -> discoverDto.mapToDomain() }
  }

  override suspend fun getEventsBasedOnIdsAndDate(
    ids: List<String>,
    date: Double,
  ): Result<List<EventDate>, DataError.Network> {
    val timeZone = TimeZone.currentSystemDefault()
    val startDateTime = date.toLocalDateTime()?.date?.atStartOfDayIn(timeZone)
    val nextDayDateTime = startDateTime?.plus(
      value = TOTAL_DAY_HOURS,
      unit = DateTimeUnit.HOUR,
    )?.toLocalDateTime(timeZone)?.getDateTimeString()
    if (startDateTime == null || nextDayDateTime == null) return Result.Error(DataError.Network.SERVER_ERROR)

    return httpClient.get<TicketMasterEventsDto>(
      route = EVENTS,
      baseUrl = BASE_URL,
      queryParameters = buildMap {
        put(EVENT_API_KEY, API_KEY)
        put(ID, ids.joinToString(","))
        put(
          START_END_DATE_TIME,
          "${startDateTime.toLocalDateTime(timeZone)?.getDateTimeString()},$nextDayDateTime"
        )
        put(SIZE, EVENT_SEARCH_SIZE)
      },
    ).map { discoverDto -> discoverDto.eventsEmbeddedDto.events.map { it.mapToDomain() } }
  }

  private fun DiscoverEventsDto.mapToDomain() = EventDate(
    id = id,
    title = name,
    date = datesDto?.startDto?.dateTime?.toLocalDateTime(),
    url = url,
  )

  companion object {
    private const val ID = "id"
    private const val KEYWORD = "keyword"
    private const val SIZE = "size"
    private const val TOTAL_DAY_HOURS = 24
    private const val EVENT_SEARCH_SIZE = 200
    private const val PREFERRED_COUNTRY = "preferredCountry"
    private const val GEO_POINT = "geoPoint"
    private const val EVENT_API_KEY = "apikey"
    private const val START_END_DATE_TIME = "localStartDateTime"
  }
}