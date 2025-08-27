package com.cjproductions.vicinity.eventDetail.presentation.eventDetail

sealed interface EventDetailAction {
  data class OnVenueClicked(val venueId: String): EventDetailAction
  data object OnRetryClicked: EventDetailAction
  data class OnToggleLike(
    val isLiked: Boolean,
    val image: String?,
  ): EventDetailAction

  data class OnDateClicked(val eventIds: List<String>, val date: Double): EventDetailAction
  data object OnChangeVenueClicked: EventDetailAction
  data object OnShareClicked: EventDetailAction
}