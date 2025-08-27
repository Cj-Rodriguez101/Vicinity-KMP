package com.cjproductions.vicinity.eventDetail.presentation.eventDates

import kotlinx.datetime.LocalDateTime

sealed class EventDateState {
  data object Loading: EventDateState()
  data class Loaded(
    val headerDate: LocalDateTime? = null,
    val eventDates: List<EventDateUI> = emptyList(),
  ): EventDateState()

  data object Error: EventDateState()
}