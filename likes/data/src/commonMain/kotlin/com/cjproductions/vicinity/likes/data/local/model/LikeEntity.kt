package com.cjproductions.vicinity.likes.data.local.model

import com.cjproductions.vicinity.likes.domain.model.Like
import comcjproductionsvicinitycoredatadatabase.LikeEntity

internal fun LikeEntity.toLike() = Like(
  userId = userId,
  title = title,
  image = image,
  category = category,
  startDateTime = startDateTime,
  endDateTime = endDateTime,
  normalizedTitle = normalizedTitle,
)

internal fun List<LikeEntity>.toLikes() = this.map { it.toLike() }