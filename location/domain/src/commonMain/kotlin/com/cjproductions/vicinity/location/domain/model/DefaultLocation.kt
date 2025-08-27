package com.cjproductions.vicinity.location.domain.model

data class DefaultLocation(
  val latitude: Double,
  val longitude: Double,
  val countryCode: String,
  val city: String?,
  val country: String?,
)

fun DefaultLocation.getPlaceName(): String {
  return city ?: country ?: ""
}

fun DefaultLocation.toUserSetLocation() = UserSetLocation(
  latitude = latitude,
  longitude = longitude,
  countryCode = countryCode,
  country = country,
  city = city,
)
