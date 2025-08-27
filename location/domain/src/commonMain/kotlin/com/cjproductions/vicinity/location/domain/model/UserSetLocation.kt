package com.cjproductions.vicinity.location.domain.model

data class UserSetLocation(
  val latitude: Double? = null,
  val longitude: Double? = null,
  val countryCode: String,
  val city: String? = null,
  val country: String?,
)

fun UserSetLocation.getPlaceName(): String {
  return city ?: country.orEmpty()
}