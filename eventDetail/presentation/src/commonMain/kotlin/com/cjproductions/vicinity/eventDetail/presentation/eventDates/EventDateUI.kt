package com.cjproductions.vicinity.eventDetail.presentation.eventDates

import com.cjproductions.vicinity.eventDetail.domain.EventDate
import com.cjproductions.vicinity.support.tools.toDateTime

data class EventDateUI(
  val id: String,
  val title: String,
  val date: String?,
  val url: String? = null,
)

private fun EventDate.toEventDateUI() = EventDateUI(
  id = id,
  title = title,
  date = date?.toDateTime()?.let { dateTime -> "${dateTime.date} ${dateTime.time}" },
  url = url,
)

internal fun List<EventDate>.toEventDateUIs() = map { it.toEventDateUI() }