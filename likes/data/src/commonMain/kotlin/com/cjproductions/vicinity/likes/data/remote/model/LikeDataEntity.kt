package com.cjproductions.vicinity.likes.data.remote.model

import com.cjproductions.vicinity.likes.domain.model.Like
import com.cjproductions.vicinity.support.tools.toDouble
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LikeDataEntity(
  @SerialName("user_id")
  val userId: String,
  @SerialName("normalized_title")
  val normalizedTitle: String,
  @SerialName("title")
  val title: String,
  @SerialName("category")
  val category: String,
  @SerialName("created_at")
  val createdAt: String? = null,
  @SerialName("image")
  val image: String? = null,
  @SerialName("start_time")
  val startDateTime: Double? = null,
  @SerialName("end_time")
  val endDateTime: Double? = null,
)

internal fun Like.toLikeDataEntity() = LikeDataEntity(
  userId = userId,
  title = title,
  normalizedTitle = normalizedTitle,
  category = category,
  startDateTime = startDateTime,
  endDateTime = endDateTime,
  image = image,
)

private fun LikeDataEntity.toLike() = Like(
  userId = userId,
  title = title,
  normalizedTitle = normalizedTitle,
  category = category,
  startDateTime = startDateTime,
  endDateTime = endDateTime,
  image = image,
  createdAt = createdAt?.toDateTime()?.toDouble() ?: 0.0
)

fun String.toDateTime(): LocalDateTime? {
  return try {
    val cleanedString = this.replace(Regex("[+\\-]\\d{2}:\\d{2}$"), "")
      .replace("Z$".toRegex(), "")

    LocalDateTime.parse(cleanedString)
  } catch (e: Exception) {
    println("toLocalDateTime: ${e.message}")
    null
  }
}

internal fun List<LikeDataEntity>.toLikes(): List<Like> {
  return this.map { it.toLike() }
}