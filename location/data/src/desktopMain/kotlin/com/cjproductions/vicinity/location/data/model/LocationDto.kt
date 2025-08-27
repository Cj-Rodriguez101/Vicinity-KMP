package com.cjproductions.vicinity.location.data.model

import com.cjproductions.vicinity.location.domain.model.DefaultLocation
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LocationDto(
  @SerialName("country") val country: String,
  @SerialName("countryCode") val countryCode: String,
  @SerialName("city") val city: String,
  @SerialName("lat") val lat: Double,
  @SerialName("lon") val lon: Double,
)

internal fun LocationDto.toDefaultLocation() = DefaultLocation(
  latitude = lat,
  longitude = lon,
  country = country,
  city = city,
  countryCode = countryCode.lowercase(),
)