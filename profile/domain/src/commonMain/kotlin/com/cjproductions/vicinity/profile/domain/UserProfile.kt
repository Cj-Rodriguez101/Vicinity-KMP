package com.cjproductions.vicinity.profile.domain

data class UserProfile(
  val id: String,
  val name: String,
  val provider: String,
  val email: String,
  val bio: String? = null,
  val image: String? = null,
  val interests: String? = null,
  val updatedAt: Double,
)