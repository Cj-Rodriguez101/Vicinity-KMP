package com.cjproductions.vicinity.support.search

data class SearchParams(
  val normalizedName: String,
  val keyword: String?,
  val location: String?,
  val latitude: Double?,
  val longitude: Double?,
)