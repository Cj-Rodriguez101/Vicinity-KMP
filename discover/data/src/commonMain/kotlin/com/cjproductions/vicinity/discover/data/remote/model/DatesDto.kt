package com.cjproductions.vicinity.discover.data.remote.model

import com.cjproductions.vicinity.discover.domain.model.Dates
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DatesDto(
  @SerialName("start") val startDto: StartDto? = StartDto(),
  @SerialName("timezone") val timezone: String? = null,
  @SerialName("status") val statusDto: StatusDto? = StatusDto()
)

internal fun DatesDto.mapToDomain() = Dates(
  start = startDto?.mapToDomain(),
  timezone = timezone,
)