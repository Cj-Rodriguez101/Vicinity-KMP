package com.cjproductions.vicinity.discover.domain.model

import kotlinx.datetime.LocalDateTime

data class User(
  val id: String,
  val username: String,
  val displayName: String,
  val profilePictureUrl: String? = null,
  val bio: String? = null,
  val joinedAt: LocalDateTime,
)