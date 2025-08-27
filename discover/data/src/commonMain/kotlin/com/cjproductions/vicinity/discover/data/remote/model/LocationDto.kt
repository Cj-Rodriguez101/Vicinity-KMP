package com.cjproductions.vicinity.discover.data.remote.model

import com.cjproductions.vicinity.discover.domain.model.VLocation
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LocationDto(
  @SerialName("longitude") val longitude: String,
  @SerialName("latitude") val latitude: String
)

internal fun LocationDto.toDomain() = VLocation(
  longitude = longitude,
  latitude = latitude
)