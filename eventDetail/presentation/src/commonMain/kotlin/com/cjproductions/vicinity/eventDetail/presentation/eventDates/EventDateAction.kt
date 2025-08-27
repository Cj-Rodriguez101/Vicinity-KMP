package com.cjproductions.vicinity.eventDetail.presentation.eventDates

sealed interface EventDateAction {
  data object OnRetryClicked: EventDateAction
}