package com.cjproductions.vicinity.discover.data.remote.model

import com.cjproductions.vicinity.discover.domain.model.Start
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StartDto(
  @SerialName("localDate") val localDate: String? = null,
  @SerialName("dateTime") val dateTime: String? = null,
  @SerialName("dateTBD") val dateTBD: Boolean? = null,
  @SerialName("dateTBA") val dateTBA: Boolean? = null,
  @SerialName("timeTBA") val timeTBA: Boolean? = null,
  @SerialName("noSpecificTime") val noSpecificTime: Boolean? = null
)

fun StartDto.mapToDomain() = Start(
  dateTime = dateTime,
  dateTBD = dateTBD,
  dateTBA = dateTBA,
  timeTBA = timeTBA,
  noSpecificTime = noSpecificTime,
)