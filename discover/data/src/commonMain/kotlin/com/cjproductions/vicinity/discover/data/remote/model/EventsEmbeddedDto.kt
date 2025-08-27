package com.cjproductions.vicinity.discover.data.remote.model

import com.cjproductions.vicinity.discover.domain.model.EventsEmbedded
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EventsEmbeddedDto(
    @SerialName("events") val events: List<DiscoverEventsDto> = listOf(),
)

fun EventsEmbeddedDto.mapToDomain() =
    EventsEmbedded(events = events.map { it.mapToDomain() })