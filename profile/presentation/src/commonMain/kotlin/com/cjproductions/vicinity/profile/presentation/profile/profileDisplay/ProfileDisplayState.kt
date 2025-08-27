package com.cjproductions.vicinity.profile.presentation.profile.profileDisplay

import com.cjproductions.vicinity.profile.presentation.profile.components.AvatarData

data class ProfileDisplayState(
  val avatarData: AvatarData = AvatarData(),
  val id: String? = null,
  val name: String? = null,
  val email: String? = null,
  val bio: String? = null,
  val location: String? = null,
  val provider: String = "",
  val likeCount: Int = 0,
  val updatedAt: Double = 0.0,
  val loading: Boolean = false,
)