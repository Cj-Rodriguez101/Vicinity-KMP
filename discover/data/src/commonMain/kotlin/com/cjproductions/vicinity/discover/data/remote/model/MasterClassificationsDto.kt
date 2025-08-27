package com.cjproductions.vicinity.discover.data.remote.model

import com.cjproductions.vicinity.discover.domain.model.ClassificationFeed
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MasterClassificationsDto(
  @SerialName("_links") val linksDto: LinksDto = LinksDto(),
  @SerialName("_embedded") val classificationEmbeddedDto: ClassificationEmbeddedDto = ClassificationEmbeddedDto(),
  @SerialName("page") val pageDto: PageDto = PageDto()
)

fun MasterClassificationsDto.mapToDomain() = ClassificationFeed(
  classificationWrapper = classificationEmbeddedDto.mapToDomain(),
  page = pageDto.mapToDomain(),
)