package com.cjproductions.vicinity.location.data.model

import com.cjproductions.vicinity.location.domain.model.UserSetLocation
import kotlinx.serialization.Serializable

@Serializable
data class UserSetLocationDto(
  val latitude: Double?,
  val longitude: Double?,
  val countryCode: String,
  val city: String?,
  val country: String?,
)

fun UserSetLocationDto.toUserSetLocation() = UserSetLocation(
  latitude = latitude,
  longitude = longitude,
  countryCode = countryCode,
  city = city,
  country = country,
)

fun UserSetLocation.toUserSetLocationDto() = UserSetLocationDto(
  latitude = latitude,
  longitude = longitude,
  countryCode = countryCode,
  city = city,
  country = country,
)