package com.cjproductions.vicinity.discover.data.remote.model

import com.cjproductions.vicinity.discover.domain.model.Classification
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ClassificationsDto(
  @SerialName("segment") val segmentDto: SegmentDto? = null,
)

fun ClassificationsDto.mapToDomain() = segmentDto?.mapToDomain()?.let { segment ->
  Classification(name = segment.name)
}