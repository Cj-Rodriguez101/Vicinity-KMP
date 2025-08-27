package com.cjproductions.vicinity.eventDetail.domain

import com.cjproductions.vicinity.core.domain.util.DataError
import com.cjproductions.vicinity.core.domain.util.Result
import com.cjproductions.vicinity.discover.domain.model.Venue
import com.cjproductions.vicinity.location.domain.model.UserLocation

interface EventDetailRepository {
    suspend fun getEventsBasedOnKeyword(
        keyword: String,
        location: UserLocation?,
    ): Result<EventVenueDetail, DataError.Network>

    suspend fun getEventsBasedOnIdsAndDate(
        ids: List<String>,
        date: Double,
    ): Result<List<EventDate>, DataError.Network>
}

data class EventVenueDetail(
    val eventDetail: EventDetail,
    val venues: List<Venue>,
    val venueEventItems: Map<String, List<EventSchedule>>,
)