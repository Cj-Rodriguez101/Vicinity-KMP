package com.cjproductions.vicinity.eventDetail.presentation.eventDetail

import com.cjproductions.vicinity.core.domain.util.DataError
import com.cjproductions.vicinity.eventDetail.presentation.eventDetail.model.EventUi
import com.cjproductions.vicinity.eventDetail.presentation.eventDetail.model.VenueUi

sealed class EventDetailViewState {
  data object Loading: EventDetailViewState()
  data class Loaded(
    val venues: List<VenueUi>,
    val selectedVenueId: String? = null,
    val events: List<EventScheduleUI>?,
    val selectedEvent: EventUi,
  ): EventDetailViewState()

  data class Error(val error: DataError): EventDetailViewState()
}