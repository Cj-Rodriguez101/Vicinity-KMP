package com.cjproductions.vicinity.discover.data.remote.model

import com.cjproductions.vicinity.discover.domain.model.Image
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImagesDto(
    @SerialName("ratio") val ratio: String = "",
    @SerialName("url") val url: String = "",
    @SerialName("width") val width: Int = 0,
    @SerialName("height") val height: Int = 0,
)

fun ImagesDto.mapToDomain() = Image(
    ratio = ratio,
    url = url,
    width = width,
    height = height,
)