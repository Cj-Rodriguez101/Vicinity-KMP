package com.cjproductions.vicinity.location.data.model

import com.cjproductions.vicinity.location.domain.model.DefaultLocation
import kotlinx.serialization.Serializable

@Serializable
data class DefaultLocationDto(
  val latitude: Double,
  val longitude: Double,
  val countryCode: String,
  val city: String?,
  val country: String?,
)

fun DefaultLocationDto.toDefaultLocation() = DefaultLocation(
  latitude = latitude,
  longitude = longitude,
  countryCode = countryCode,
  city = city,
  country = country,
)

fun DefaultLocation.toDefaultLocationDto() = DefaultLocationDto(
  latitude = latitude,
  longitude = longitude,
  countryCode = countryCode,
  city = city,
  country = country,
)

