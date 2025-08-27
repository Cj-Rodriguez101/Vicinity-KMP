package com.cjproductions.vicinity.eventDetail.presentation.eventDetail

sealed class EventDetailDestination {
  data class EventDates(
    val ids: List<String>,
    val date: Double,
  ): EventDetailDestination()
}