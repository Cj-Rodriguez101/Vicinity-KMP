package com.cjproductions.vicinity.search.domain.model

data class RecentSearch(
  val normalizedName: String,
  val name: String?,
  val location: String?,
  val latitude: Double? = null,
  val longitude: Double? = null,
)