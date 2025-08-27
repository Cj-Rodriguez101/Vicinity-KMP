package com.cjproductions.vicinity.core.presentation.ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.XXXXLarge
import com.cjproductions.vicinity.core.presentation.ui.theme.VicinityTheme
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Info

@Composable
fun ImageErrorContainer(modifier: Modifier = Modifier) {
  Box(
    contentAlignment = Alignment.Center,
    modifier = modifier,
  ) {
    Icon(
      imageVector = FontAwesomeIcons.Solid.Info,
      contentDescription = null,
      tint = Color.Red,
      modifier = Modifier.size(XXXXLarge.dp)
    )
  }
}

@Preview
@Composable
fun ImageErrorContainerPreview() {
  VicinityTheme {
    ImageErrorContainer(
      modifier = Modifier
        .fillMaxWidth()
        .height(200.dp)
        .clip(MaterialTheme.shapes.medium),
    )
  }
}