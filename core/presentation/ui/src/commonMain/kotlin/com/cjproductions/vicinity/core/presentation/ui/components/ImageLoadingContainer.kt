package com.cjproductions.vicinity.core.presentation.ui.components

import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.valentinilk.shimmer.shimmer

@Composable
fun ImageLoadingContainer(
  modifier: Modifier = Modifier,
) {
  Surface(
    shape = MaterialTheme.shapes.medium,
    color = Color.Gray,
    modifier = modifier
      .shimmer(),
  ) { }
}