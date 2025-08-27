package com.cjproductions.vicinity.profile.data.user.model

import com.cjproductions.vicinity.profile.domain.UserProfile
import com.cjproductions.vicinity.support.tools.toDouble
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDataEntity(
  @SerialName("user_id")
  val userId: String = "",
  @SerialName("name")
  val name: String,
  @SerialName("provider")
  val provider: String,
  @SerialName("email")
  val email: String,
  @SerialName("bio")
  val bio: String? = null,
  @SerialName("image")
  val image: String? = null,
  @SerialName("interests")
  val interests: String? = null,
  @SerialName("updated_at")
  val updatedAt: String = "",
)

internal fun UserDataEntity.toUserDomainEntity(): UserProfile {
  val time = updatedAt.toDateTime()?.toDouble() ?: 0.0
  return UserProfile(
    id = userId,
    name = name,
    provider = provider,
    email = email,
    bio = bio,
    image = image,
    interests = interests,
    updatedAt = time,
  )
}

fun UserProfile.toUserDataEntity(): UserDataEntity {
  return UserDataEntity(
    userId = id,
    name = name,
    provider = provider,
    email = email,
    bio = bio,
    image = image,
    interests = interests,
    updatedAt = updatedAt.toString(),
  )
}

fun String.toDateTime(): LocalDateTime? {
  return try {
    Instant.parse(this).toLocalDateTime(TimeZone.currentSystemDefault())
  } catch (e: Exception) {
    println("Error converting date: $e")
    null
  }
}
