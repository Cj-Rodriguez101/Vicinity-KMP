package com.cjproductions.vicinity.eventDetail.data.remote.model

import com.cjproductions.vicinity.discover.data.remote.model.AttractionsDto
import com.cjproductions.vicinity.discover.data.remote.model.ClassificationsDto
import com.cjproductions.vicinity.discover.data.remote.model.DatesDto
import com.cjproductions.vicinity.discover.data.remote.model.ImagesDto
import com.cjproductions.vicinity.discover.data.remote.model.VenuesDto
import com.cjproductions.vicinity.discover.data.remote.model.mapToDomain
import com.cjproductions.vicinity.discover.data.remote.model.toStatusCode
import com.cjproductions.vicinity.eventDetail.domain.EventDetail
import com.cjproductions.vicinity.support.tools.toLocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EventDetailDto(
    @SerialName("name") val name: String = "",
    @SerialName("id") val id: String = "",
    @SerialName("url") val url: String? = null,
    @SerialName("locale") val locale: String = "",
    @SerialName("images") val images: List<ImagesDto> = listOf(),
    @SerialName("dates") val datesDto: DatesDto? = DatesDto(),
    @SerialName("classifications") val classificationsDto: List<ClassificationsDto> = listOf(),
    @SerialName("info") val info: String? = null,
    @SerialName("pleaseNote") val pleaseNote: String? = null,
    @SerialName("_embedded") val embeddedVenueDto: EmbeddedVenueDto? = EmbeddedVenueDto(),
)

@Serializable
data class EmbeddedVenueDto(
    @SerialName("venues") val venues: List<VenuesDto> = listOf(),
    @SerialName("attractions") val attractions: List<AttractionsDto> = listOf(),
)

fun EventDetailDto.mapToDomain(): EventDetail {
    val allImages = images.map { it.mapToDomain() }
    val mainImage = images
        .filter { it.ratio == MAX_IMAGE_RATIO }
        .maxByOrNull { it.width }?.url
    val otherImages = allImages.drop(1).distinct()

    return EventDetail(
        id = id,
        title = name,
        locale = locale,
        mainImage = mainImage,
        otherImages = otherImages.map { it.url },
        url = url,
        info = info,
        pleaseNote = pleaseNote,
        startDateTime = datesDto?.startDto?.dateTime?.toLocalDateTime(),
        status = datesDto?.statusDto?.code.toStatusCode(),
        classifications = classificationsDto.mapNotNull { it.mapToDomain() },
        venues = embeddedVenueDto?.venues?.map { it.mapToDomain() }.orEmpty(),
        attractions = embeddedVenueDto?.attractions?.map { it.mapToDomain() }.orEmpty()
    )
}

private const val MAX_IMAGE_RATIO = "16_9"