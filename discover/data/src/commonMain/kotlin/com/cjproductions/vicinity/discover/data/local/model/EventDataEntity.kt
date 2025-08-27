package com.cjproductions.vicinity.discover.data.local.model

import com.cjproductions.vicinity.discover.domain.model.DiscoverEvent
import comcjproductionsvicinitycoredatadatabase.EventEntity

data class EventDataEntity(
  val category: String,
  val venueIds: String,
  val eventIds: String,
  val marketIds: String,
  val originalTitle: String,
  val imageUrl: String,
  val earliestDate: Double?,
  val latestDate: Double?,
  val createdAt: Double,
)

fun EventEntity.toEvent(): DiscoverEvent {
  return DiscoverEvent(
    title = originalTitle,
    normalizedTitle = normalizedTitle,
    category = category,
    venueIds = venueIds.split(","),
    eventIds = eventIds.split(","),
    imageUrl = imageUrl,
    earliestDate = earliestDate,
    latestDate = latestDate,
  )
}