package com.cjproductions.vicinity.location.data.model

import com.cjproductions.vicinity.location.domain.model.UserLocation
import kotlinx.serialization.Serializable

@Serializable
data class UserLocationDto(
  val defaultLocationDto: DefaultLocationDto?,
  val userSetLocationDto: UserSetLocationDto?,
  val isUserSet: Boolean,
)

fun UserLocationDto.toUserLocation() = UserLocation(
  defaultLocation = defaultLocationDto?.toDefaultLocation(),
  userSetLocation = userSetLocationDto?.toUserSetLocation(),
  isUserSet = isUserSet,
)

fun UserLocation.toUserLocationDto() = UserLocationDto(
  defaultLocationDto = defaultLocation?.toDefaultLocationDto(),
  userSetLocationDto = userSetLocation?.toUserSetLocationDto(),
  isUserSet = isUserSet,
)