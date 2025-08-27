package com.cjproductions.vicinity.core.presentation.ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize
import com.cjproductions.vicinity.core.presentation.ui.theme.VicinityTheme
import org.jetbrains.compose.resources.vectorResource
import vicinity.core.presentation.ui.generated.resources.Back
import vicinity.core.presentation.ui.generated.resources.Res

@Composable
fun RoundedButton(
  icon: ImageVector,
  backgroundColor: Color = MaterialTheme.colorScheme.surface,
  contentColor: Color = MaterialTheme.colorScheme.onSurface,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  FilledIconButton(
    onClick = onClick,
    shape = CircleShape,
    colors = IconButtonDefaults.filledIconButtonColors(
      containerColor = backgroundColor,
      contentColor = contentColor,
    ),
    modifier = modifier,
  ) {
    Icon(
      imageVector = icon,
      contentDescription = null,
      tint = contentColor,
      modifier = Modifier.size(GlobalPaddingAndSize.Medium.dp)
    )
  }
}

@Preview
@Composable
fun RoundedButtonPreview() {
  VicinityTheme {
    RoundedButton(
      icon = vectorResource(Res.drawable.Back),
      onClick = {}
    )
  }
}