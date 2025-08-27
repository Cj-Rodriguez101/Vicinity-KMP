package com.cjproductions.vicinity.geoEvents.presentation.locationSelector

import com.cjproductions.vicinity.search.domain.model.GeoCodeLocation

data class LocationSelectorState(
  val searchLocation: String? = null,
  val location: String? = null,
  val searches: List<SearchItem> = emptyList(),
  val loading: Boolean = false,
)

private fun GeoCodeLocation.toSearchItem() = SearchItem(
  discoveredCountry = displayPlace,
  latitude = latitude,
  longitude = longitude,
  countryCode = countryCode!!,
)

fun List<GeoCodeLocation>.toSearchItems() =
  filter { it.countryCode != null }.map { it.toSearchItem() }