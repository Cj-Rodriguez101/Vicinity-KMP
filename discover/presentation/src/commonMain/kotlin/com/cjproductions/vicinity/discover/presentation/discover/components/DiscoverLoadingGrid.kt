package com.cjproductions.vicinity.discover.presentation.discover.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cjproductions.vicinity.core.presentation.ui.components.RadiusVerticalGrid

@Composable
fun DiscoverLoadingGrid(modifier: Modifier = Modifier) {
  RadiusVerticalGrid(modifier = modifier) {
    items(count = 8) {
      DiscoverLoadingCard()
    }
  }
}