package com.cjproductions.vicinity.search.data.remote.model

import com.cjproductions.vicinity.search.domain.model.MapSearchEvent
import com.cjproductions.vicinity.search.domain.model.MapVenue
import com.cjproductions.vicinity.search.domain.model.SearchEvent
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PageDto(
  @SerialName("totalElements") val totalElements: Int = 0,
)


@Serializable
data class SearchEventDto(
  @SerialName("_embedded") val eventsEmbeddedDto: EventsEmbeddedDto = EventsEmbeddedDto(),
  @SerialName("page") val pageDto: PageDto = PageDto(),
)

@Serializable
data class EventsEmbeddedDto(
  @SerialName("events") val events: List<EventsDto> = listOf(),
)

@Serializable
data class EmbeddedVenueDto(
  @SerialName("venues") val venues: List<VenuesDto>? = listOf(),
)

@Serializable
data class ClassificationsDto(
  @SerialName("segment") val segmentDto: SegmentDto? = null,
)

@Serializable
data class SegmentDto(
  @SerialName("id") val id: String?,
  @SerialName("name") val name: String?,
)

@Serializable
data class ImagesDto(
  @SerialName("url") val url: String?,
)

@Serializable
data class StartDto(
  @SerialName("localDate") val localDate: String? = null,
  @SerialName("dateTime") val dateTime: String? = null,
  @SerialName("dateTBD") val dateTBD: Boolean? = null,
  @SerialName("dateTBA") val dateTBA: Boolean? = null,
  @SerialName("timeTBA") val timeTBA: Boolean? = null,
  @SerialName("noSpecificTime") val noSpecificTime: Boolean? = null,
)

@Serializable
data class DatesDto(
  @SerialName("start") val startDto: StartDto? = StartDto(),
)

@Serializable
data class EventsDto(
  @SerialName("id") val id: String,
  @SerialName("name") val name: String,
  @SerialName("dates") val datesDto: DatesDto? = null,
  @SerialName("images") val images: List<ImagesDto>? = null,
  @SerialName("classifications") val classifications: List<ClassificationsDto>? = null,
  @SerialName("_embedded") val embeddedVenueDto: EmbeddedVenueDto = EmbeddedVenueDto(),
)

@Serializable
data class VenuesDto(
  @SerialName("name") val name: String = "",
  @SerialName("id") val id: String = "",
  @SerialName("country") val countryDto: CountryDto? = CountryDto(),
  @SerialName("address") val addressDto: AddressDto? = AddressDto(),
  @SerialName("location") val locationDto: LocationDto? = null,
)

@Serializable
data class LocationDto(
  @SerialName("longitude") val longitude: String,
  @SerialName("latitude") val latitude: String,
)

@Serializable
data class AddressDto(@SerialName("line1") val line1: String = "")

@Serializable
data class CountryDto(
  @SerialName("name") val name: String = "",
  @SerialName("countryCode") val countryCode: String = "",
)

fun VenuesDto.mapToDomain() = MapVenue(
  id = id,
  name = name,
  location = "${locationDto?.latitude}, ${locationDto?.longitude}",
  lat = locationDto?.latitude?.toDouble() ?: 0.0,
  lon = locationDto?.longitude?.toDouble() ?: 0.0,
)

private fun EmbeddedVenueDto.toMapSearchEvent(): List<MapVenue> {
  return this.venues?.mapNotNull { it.mapToDomain() }.orEmpty()
}

private fun EventsDto.toMapSearchEvent(): MapSearchEvent {
  return MapSearchEvent(
    id = id,
    name = name,
    category = classifications?.firstOrNull()?.segmentDto?.name.orEmpty(),
    venue = embeddedVenueDto.toMapSearchEvent().firstOrNull(),
    image = images?.firstOrNull()?.url,
    date = datesDto?.startDto?.dateTime,
  )
}

fun SearchEventDto.toSearchEvent(): SearchEvent = SearchEvent(
  totalEvents = pageDto.totalElements,
  eventsEmbeddedDto.events.map { it.toMapSearchEvent() },
)