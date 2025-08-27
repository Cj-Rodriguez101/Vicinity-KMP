package com.cjproductions.vicinity.discover.data.remote.model

import com.cjproductions.vicinity.discover.domain.model.Address
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddressDto(@SerialName("line1") val line1: String = "")

internal fun AddressDto.mapToDomain() = Address(line1 = line1)