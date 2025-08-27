package com.cjproductions.vicinity.location.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize

@Composable
actual fun MapUI(
  lat: Double,
  long: Double,
  modifier: Modifier,
) {
  PrMapUI(
    lat = lat,
    long = long,
    modifier = modifier.padding(horizontal = GlobalPaddingAndSize.XSmall.dp),
  )
}