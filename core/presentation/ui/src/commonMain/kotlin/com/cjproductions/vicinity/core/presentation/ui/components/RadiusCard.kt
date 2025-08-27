package com.cjproductions.vicinity.core.presentation.ui.components

import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun RadiusCard(
  backgroundColor: Color = MaterialTheme.colorScheme.surface,
  modifier: Modifier = Modifier,
  content: @Composable () -> Unit,
) {
  Card(
    colors = CardDefaults.cardColors(
      containerColor = backgroundColor,
    ),
    modifier = modifier,
    shape = MaterialTheme.shapes.medium,
    content = { content() }
  )
}