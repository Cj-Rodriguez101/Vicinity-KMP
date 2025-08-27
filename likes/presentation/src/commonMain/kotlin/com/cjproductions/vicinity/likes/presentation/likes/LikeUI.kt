package com.cjproductions.vicinity.likes.presentation.likes

import com.cjproductions.vicinity.likes.domain.model.Like
import com.cjproductions.vicinity.support.tools.toLocalDateTime
import kotlinx.datetime.LocalDateTime

data class LikeUI(
  val userId: String,
  val normalizedTitle: String,
  val title: String,
  val category: String,
  val image: String? = null,
  val startDateTime: LocalDateTime?,
)

fun Like.toLikeUI() = LikeUI(
  userId = userId,
  title = title,
  category = category,
  normalizedTitle = normalizedTitle,
  image = image,
  startDateTime = startDateTime?.toLocalDateTime()
)