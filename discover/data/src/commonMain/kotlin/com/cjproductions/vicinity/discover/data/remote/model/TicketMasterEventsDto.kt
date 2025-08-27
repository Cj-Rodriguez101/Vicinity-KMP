package com.cjproductions.vicinity.discover.data.remote.model

import com.cjproductions.vicinity.discover.domain.model.DiscoverFeed
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TicketMasterEventsDto(
  @SerialName("_embedded") val eventsEmbeddedDto: EventsEmbeddedDto = EventsEmbeddedDto(),
  @SerialName("page") val pageDto: PageDto = PageDto()
)

fun TicketMasterEventsDto.mapToDomain() = DiscoverFeed(
  eventsEmbedded = eventsEmbeddedDto.mapToDomain(),
  page = pageDto.mapToDomain(),
)