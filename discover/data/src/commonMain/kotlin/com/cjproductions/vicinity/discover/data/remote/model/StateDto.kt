package com.cjproductions.vicinity.discover.data.remote.model

import com.cjproductions.vicinity.discover.domain.model.State
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StateDto(
  @SerialName("name") val name: String = "",
  @SerialName("stateCode") val stateCode: String = ""
)

internal fun StateDto.mapToDomain() = State(name = name, stateCode = stateCode)