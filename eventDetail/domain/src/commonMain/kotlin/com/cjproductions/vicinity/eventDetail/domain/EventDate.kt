package com.cjproductions.vicinity.eventDetail.domain

import kotlinx.datetime.LocalDateTime

data class EventDate(
  val id: String,
  val title: String,
  val date: LocalDateTime?,
  val url: String? = null,
)