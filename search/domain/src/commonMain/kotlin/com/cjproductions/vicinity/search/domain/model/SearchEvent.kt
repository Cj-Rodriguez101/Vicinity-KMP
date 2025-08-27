package com.cjproductions.vicinity.search.domain.model

data class SearchEvent(
  val totalEvents: Int,
  val events: List<MapSearchEvent>,
)

data class MapSearchEvent(
  val id: String,
  val name: String,
  val category: String,
  val date: String?,
  val image: String?,
  val venue: MapVenue?,
)

data class MapVenue(
  val id: String,
  val name: String,
  val location: String,
  val lat: Double,
  val lon: Double,
)