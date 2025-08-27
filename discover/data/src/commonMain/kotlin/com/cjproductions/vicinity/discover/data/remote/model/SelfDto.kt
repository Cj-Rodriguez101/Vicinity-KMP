package com.cjproductions.vicinity.discover.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SelfDto(
  @SerialName("href") val href: String? = null
)