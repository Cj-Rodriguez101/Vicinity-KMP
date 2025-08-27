package com.cjproductions.vicinity.discover.data.remote.model

import com.cjproductions.vicinity.discover.domain.model.Country
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CountryDto(
  @SerialName("name") val name: String = "",
  @SerialName("countryCode") val countryCode: String = ""
)

internal fun CountryDto.mapToDomain() = Country(name = name, countryCode = countryCode)