package com.cjproductions.vicinity.location.domain.model

data class UserLocation(
  val defaultLocation: DefaultLocation? = null,
  val userSetLocation: UserSetLocation? = null,
  val isUserSet: Boolean = false,
)

fun UserLocation.getCountryCode(): String? {
  return if (isUserSet) userSetLocation?.countryCode else defaultLocation?.countryCode
}

fun UserLocation.getCountry(): String? {
  return if (isUserSet) userSetLocation?.country else getDefaultCountry()
}

private fun UserLocation.getDefaultCountry(): String? {
  return defaultLocation?.city ?: defaultLocation?.country
}