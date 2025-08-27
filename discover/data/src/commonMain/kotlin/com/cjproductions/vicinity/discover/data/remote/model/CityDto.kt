package com.cjproductions.vicinity.discover.data.remote.model

import com.cjproductions.vicinity.discover.domain.model.City
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CityDto(@SerialName("name") val name: String = "")

internal fun CityDto.mapToDomain() = City(name = name)