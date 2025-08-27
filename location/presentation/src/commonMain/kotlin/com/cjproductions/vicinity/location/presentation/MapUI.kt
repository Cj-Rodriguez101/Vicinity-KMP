package com.cjproductions.vicinity.location.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun MapUI(
  lat: Double,
  long: Double,
  modifier: Modifier,
)