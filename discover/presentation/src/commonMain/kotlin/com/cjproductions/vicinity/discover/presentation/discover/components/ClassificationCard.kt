package com.cjproductions.vicinity.discover.presentation.discover.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import com.cjproductions.vicinity.core.presentation.ui.components.RadiusFilterChip
import com.cjproductions.vicinity.core.presentation.ui.theme.VicinityTheme
import com.cjproductions.vicinity.discover.presentation.discover.model.ClassificationUI

@Composable
fun ClassificationCard(
  classification: ClassificationUI,
  onClick: () -> Unit,
) {
  with(classification) {
    RadiusCard(
      name = name,
      isSelected = isSelected,
      onClick = onClick,
    )
  }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RadiusCard(
  name: String,
  isSelected: Boolean = false,
  textStyle: TextStyle = LocalTextStyle.current,
  onClick: (() -> Unit)? = null,
  modifier: Modifier = Modifier,
) {
  RadiusFilterChip(
    label = name,
    textStyle = textStyle,
    selected = isSelected,
    onClick = onClick ?: {},
    modifier = modifier
  )
}

@Preview
@Composable
fun RadiusCardPreview() {
  VicinityTheme {
    RadiusCard(
      name = "Music",
      isSelected = true,
      onClick = {},
    )
  }
}