package com.cjproductions.vicinity.eventDetail.domain

import kotlinx.datetime.LocalDateTime

data class EventSchedule(
  val id: String,
  val dateTimes: List<LocalDateTime>,
)