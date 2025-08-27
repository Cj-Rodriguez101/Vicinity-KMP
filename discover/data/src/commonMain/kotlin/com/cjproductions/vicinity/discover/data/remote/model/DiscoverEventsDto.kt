package com.cjproductions.vicinity.discover.data.remote.model

import com.cjproductions.vicinity.discover.domain.model.Event
import com.cjproductions.vicinity.discover.domain.model.Status
import com.cjproductions.vicinity.support.tools.toLocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DiscoverEventsDto(
  @SerialName("name") val name: String = "",
  @SerialName("type") val type: String = "",
  @SerialName("id") val id: String = "",
  @SerialName("url") val url: String? = null,
  @SerialName("locale") val locale: String = "",
  @SerialName("images") val images: List<ImagesDto>? = null,
  @SerialName("dates") val datesDto: DatesDto? = null,
  @SerialName("classifications") val classifications: List<ClassificationsDto>? = null,
  @SerialName("_embedded") val embeddedVenueDto: EmbeddedVenueDto = EmbeddedVenueDto(),
)

@Serializable
data class EmbeddedVenueDto(
  @SerialName("venues") val venues: List<VenuesDto>? = listOf(),
)

fun DiscoverEventsDto.mapToDomain(): Event {
  return Event(
    id = id,
    title = name,
    locale = locale,
    type = type,
    image = images?.filter { it.ratio == "16_9" }?.maxByOrNull { it.width }?.url,
    url = url,
    dateTimes = datesDto?.startDto?.dateTime?.toLocalDateTime()?.let { listOf(it) }.orEmpty(),
    status = datesDto?.statusDto?.code?.toStatusCode() ?: Status.UNKNOWN,
    venue = embeddedVenueDto.venues?.firstOrNull()?.mapToDomain(),
    classifications = this.classifications?.mapNotNull { it.mapToDomain() }.orEmpty(),
  )
}

fun String?.toStatusCode(): Status {
  return when (this) {
    "onsale" -> Status.ON_SALE
    else -> Status.UNKNOWN
  }
}