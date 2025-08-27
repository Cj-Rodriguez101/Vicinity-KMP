package com.cjproductions.vicinity.core.presentation.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight

@Composable
fun HeaderContent(
  text: String,
  modifier: Modifier = Modifier,
) {
  Text(
    text = text,
    style = MaterialTheme.typography.titleLarge,
    fontWeight = FontWeight.Bold,
    modifier = modifier,
  )
}