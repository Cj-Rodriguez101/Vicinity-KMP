package com.cjproductions.vicinity.eventDetail.presentation.eventDetail

import com.cjproductions.vicinity.eventDetail.domain.EventSchedule
import kotlinx.datetime.LocalDateTime

data class EventScheduleUI(
  val id: String,
  val dateTimes: List<LocalDateTime>,
)

internal fun EventSchedule.toEventItemUI() = EventScheduleUI(
  id = id,
  dateTimes = dateTimes,
)

internal fun List<EventSchedule>.toEventItemUIs() = map { it.toEventItemUI() }