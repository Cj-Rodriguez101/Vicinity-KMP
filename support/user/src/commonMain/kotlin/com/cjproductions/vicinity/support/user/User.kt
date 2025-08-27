package com.cjproductions.vicinity.support.user

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class User(
  val id: String,
  val username: String,
  val email: String,
  val provider: Provider,
  val authenticated: Boolean = false,
  val createdAt: LocalDateTime?,
)

@Serializable
enum class Provider {
  Email,
  Google,
  Apple,
}
