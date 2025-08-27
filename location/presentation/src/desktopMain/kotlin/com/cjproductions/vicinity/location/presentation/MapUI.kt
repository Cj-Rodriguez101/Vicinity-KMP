package com.cjproductions.vicinity.location.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ovh.plrapps.mapcompose.api.ExperimentalClusteringApi

@OptIn(ExperimentalClusteringApi::class)
@Composable
actual fun MapUI(
  lat: Double,
  long: Double,
  modifier: Modifier,
) {
  PrMapUI(
    lat = lat,
    long = long,
    modifier = modifier,
  )
}