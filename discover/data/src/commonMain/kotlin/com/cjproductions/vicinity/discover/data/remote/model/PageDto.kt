package com.cjproductions.vicinity.discover.data.remote.model

import com.cjproductions.vicinity.discover.domain.model.Page
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PageDto(
  @SerialName("size") val size: Int = 0,
  @SerialName("totalElements") val totalElements: Int = 0,
  @SerialName("totalPages") val totalPages: Int = 0,
  @SerialName("number") val number: Int = 0
)

internal fun PageDto.mapToDomain() = Page(
  size = size,
  totalElements = totalElements,
  totalPages = totalPages,
  number = number,
)