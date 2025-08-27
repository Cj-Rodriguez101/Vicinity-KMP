package com.cjproductions.vicinity.search.domain.model

data class GeoCodeLocation(
  val latitude: Double,
  val longitude: Double,
  val displayPlace: String,
  val country: String,
  val countryCode: String?,
)