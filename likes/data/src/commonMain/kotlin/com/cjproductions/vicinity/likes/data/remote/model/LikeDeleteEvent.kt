package com.cjproductions.vicinity.likes.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LikeDeleteEvent(
  @SerialName("user_id") val userId: String,
  @SerialName("normalized_title") val normalizedTitle: String,
)