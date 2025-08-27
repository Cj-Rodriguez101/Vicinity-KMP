package com.cjproductions.vicinity.discover.data.remote.model

import com.cjproductions.vicinity.discover.domain.model.ClassificationWrapper
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ClassificationEmbeddedDto(
  @SerialName("classifications") val classifications: List<ClassificationsDto>? = null
)

internal fun ClassificationEmbeddedDto.mapToDomain() =
  ClassificationWrapper(classification = classifications?.mapNotNull { it.mapToDomain() })