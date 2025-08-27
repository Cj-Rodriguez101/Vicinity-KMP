package com.cjproductions.vicinity.discover.presentation.countrySelector

import androidx.compose.ui.graphics.vector.ImageVector
import com.cjproductions.vicinity.location.domain.model.CountryInfo
import dev.carlsen.flagkit.FlagKit

data class CountryUI(
  val countryCode: String,
  val name: String,
  val image: ImageVector?,
  val latitude: Double = 0.0,
  val longitude: Double = 0.0,
)

fun CountryInfo.toCountryUI() = CountryUI(
  countryCode = countryCode,
  name = name,
  image = FlagKit.getFlag(countryCode),
  latitude = latitude,
  longitude = longitude,
)

fun CountryUI.toCountryInfo() = CountryInfo(
  countryCode = countryCode,
  name = name,
  latitude = latitude,
  longitude = longitude,
)