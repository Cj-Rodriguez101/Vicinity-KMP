package com.cjproductions.vicinity.discover.data.remote.model

import com.cjproductions.vicinity.discover.domain.model.Attraction
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AttractionsDto(
  @SerialName("name") val name: String? = null,
  @SerialName("type") val type: String,
  @SerialName("id") val id: String = "",
  @SerialName("images") val images: List<ImagesDto> = listOf(),
  @SerialName("classifications") val classifications: List<ClassificationsDto> = listOf(),
)

fun AttractionsDto.mapToDomain() = Attraction(
  name = name.orEmpty(),
  id = id,
  type = type,
  images = images.map { it.mapToDomain() },
)