package com.cjproductions.vicinity.search.data.remote.model

import com.cjproductions.vicinity.search.domain.model.GeoCodeLocation
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GeoCodeLocationDto(
  @SerialName("lat")
  val latitude: String,

  @SerialName("lon")
  val longitude: String,

  @SerialName("display_place")
  val displayPlace: String,

  @SerialName("address")
  val address: Address,
)

@Serializable
data class Address(
  @SerialName("country")
  val country: String? = null,

  @SerialName("country_code")
  val countryCode: String? = null,
)

private fun GeoCodeLocationDto.toGeoCodeLocation() = GeoCodeLocation(
  latitude = latitude.toDoubleOrNull() ?: 0.0,
  longitude = longitude.toDoubleOrNull() ?: 0.0,
  displayPlace = displayPlace,
  country = address.country ?: displayPlace,
  countryCode = address.countryCode,
)

fun List<GeoCodeLocationDto>.toGeoCodeLocations() = map { it.toGeoCodeLocation() }