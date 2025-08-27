package com.cjproductions.vicinity.likes.domain.model

data class Like(
  val userId: String,
  val normalizedTitle: String,
  val title: String,
  val category: String = "",
  val image: String? = null,
  val startDateTime: Double? = null,
  val endDateTime: Double? = null,
  val createdAt: Double? = null,
)