package com.cjproductions.vicinity.discover.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StatusDto(
  @SerialName("code") val code: String? = null
)