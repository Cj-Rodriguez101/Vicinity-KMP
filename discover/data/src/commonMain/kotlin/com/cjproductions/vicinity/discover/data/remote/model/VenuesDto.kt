package com.cjproductions.vicinity.discover.data.remote.model

import com.cjproductions.vicinity.discover.domain.model.Venue
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VenuesDto(
    @SerialName("name") val name: String = "",
    @SerialName("id") val id: String = "",
    @SerialName("city") val cityDto: CityDto? = CityDto(),
    @SerialName("state") val stateDto: StateDto? = StateDto(),
    @SerialName("country") val countryDto: CountryDto? = CountryDto(),
    @SerialName("address") val addressDto: AddressDto? = AddressDto(),
    @SerialName("location") val locationDto: LocationDto? = null,
    @SerialName("markets") val markets: List<MarketsDto> = listOf(),
    @SerialName("images") val images: List<ImagesDto>? = null,
)

fun VenuesDto.mapToDomain() = Venue(
    name = name,
    id = id,
    marketIds = markets.mapNotNull { it.id },
    city = cityDto?.name,
    state = stateDto?.name,
    country = countryDto?.name,
    location = locationDto?.toDomain(),
    images = images?.map { it.mapToDomain() },
)