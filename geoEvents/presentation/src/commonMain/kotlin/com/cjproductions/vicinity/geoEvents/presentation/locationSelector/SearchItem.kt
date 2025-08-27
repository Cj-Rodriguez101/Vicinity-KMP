package com.cjproductions.vicinity.geoEvents.presentation.locationSelector

data class SearchItem(
  val latitude: Double,
  val longitude: Double,
  val discoveredCountry: String,
  val countryCode: String,
)