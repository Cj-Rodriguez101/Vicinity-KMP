package com.cjproductions.vicinity.eventDetail.data

import com.cjproductions.vicinity.core.domain.util.DataError
import com.cjproductions.vicinity.core.domain.util.Result
import com.cjproductions.vicinity.discover.domain.model.Event
import com.cjproductions.vicinity.eventDetail.data.remote.RemoteEventDetailDataSource
import com.cjproductions.vicinity.eventDetail.domain.EventDate
import com.cjproductions.vicinity.eventDetail.domain.EventDetailRepository
import com.cjproductions.vicinity.eventDetail.domain.EventSchedule
import com.cjproductions.vicinity.eventDetail.domain.EventVenueDetail
import com.cjproductions.vicinity.location.domain.model.UserLocation
import com.cjproductions.vicinity.support.tools.AppDispatcher
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.withContext

class DefaultEventDetailRepository(
  private val remoteEventDetailDataSource: RemoteEventDetailDataSource,
  private val appDispatcher: AppDispatcher,
): EventDetailRepository {

  override suspend fun getEventsBasedOnKeyword(
    keyword: String,
    location: UserLocation?,
  ): Result<EventVenueDetail, DataError.Network> = withContext(appDispatcher.io) {
    try {
      val discoverFeed = remoteEventDetailDataSource.getEventsBasedOnKeyword(
        keyword = keyword,
        location = location,
      ).getOrNull() ?: return@withContext Result.Error(DataError.Network.UNKNOWN)

      val events = discoverFeed.eventsEmbedded.events

      val venues =
        events.mapNotNull { it.venue }
          .filter { it.location != null && it.name.isNotEmpty() }.distinctBy { it.id }

      val eventByVenue = events
        .filter { it.venue != null }
        .groupBy { it.venue!!.id }
        .mapValues { (_, events) ->
          events.filter { it.dateTimes.isNotEmpty() }.map { event -> event.toEventItem() }
        }

      val eventDetail =
        events.map { it.id }.firstOrNull()
          ?.let { remoteEventDetailDataSource.getEventDetail(it) }
          ?.getOrNull()
          ?: return@withContext Result.Error(DataError.Network.UNKNOWN)

      Result.Success(
        EventVenueDetail(
          eventDetail = eventDetail,
          venues = venues,
          venueEventItems = eventByVenue,
        )
      )
    } catch (ex: Exception) {
      if (ex is CancellationException) throw ex
      Result.Error(DataError.Network.UNKNOWN)
    }
  }

  override suspend fun getEventsBasedOnIdsAndDate(
    ids: List<String>,
    date: Double,
  ): Result<List<EventDate>, DataError.Network> = withContext(appDispatcher.io) {
    try {
      remoteEventDetailDataSource.getEventsBasedOnIdsAndDate(
        ids = ids,
        date = date,
      )
    } catch (ex: Exception) {
      if (ex is CancellationException) throw ex
      Result.Error(DataError.Network.UNKNOWN)
    }
  }
}

private fun Event.toEventItem() = EventSchedule(
  id = id,
  dateTimes = this@toEventItem.dateTimes,
)