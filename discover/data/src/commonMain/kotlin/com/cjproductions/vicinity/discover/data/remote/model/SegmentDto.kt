package com.cjproductions.vicinity.discover.data.remote.model

import com.cjproductions.vicinity.discover.domain.model.Segment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SegmentDto(
    @SerialName("id") val id: String?,
    @SerialName("name") val name: String?,
)

internal fun SegmentDto.mapToDomain(): Segment? {
    if (id == null || name == null) return null
    return Segment(
        name = name,
        id = id
    )
}