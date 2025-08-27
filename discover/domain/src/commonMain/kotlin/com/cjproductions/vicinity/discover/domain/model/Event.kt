package com.cjproductions.vicinity.discover.domain.model

import kotlinx.datetime.LocalDateTime

data class Event(
  val id: String,
  val title: String,
  val locale: String,
  val type: String,
  val url: String? = null,
  val image: String? = null,
  val dateTimes: List<LocalDateTime>,
  val status: Status,
  val venue: Venue?,
  val classifications: List<Classification>,
)

enum class Status {
  ON_SALE,
  UNKNOWN,
}

data class DiscoverEvent(
  val title: String,
  val normalizedTitle: String,
  val category: String,
  val venueIds: List<String>,
  val eventIds: List<String>,
  val imageUrl: String,
  val earliestDate: Double?,
  val latestDate: Double?,
)