package com.cjproductions.vicinity.core.presentation.ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.Large
import dev.carlsen.flagkit.FlagIcons
import dev.carlsen.flagkit.flagicons.IO
import dev.carlsen.flagkit.flagicons.US

@Composable
fun FlagCircle(
  flag: ImageVector?,
  onClick: () -> Unit,
) {
  Image(
    imageVector = flag ?: FlagIcons.US,
    contentDescription = null,
    contentScale = ContentScale.FillBounds,
    modifier = Modifier
      .size(Large.dp)
      .clip(CircleShape)
      .border(
        width = GlobalPaddingAndSize.XXXSmall.dp,
        color = MaterialTheme.colorScheme.onSurface,
        shape = CircleShape
      ).clickable(onClick = onClick)
  )
}

@Preview
@Composable
fun FlagCirclePreview() {
  FlagCircle(
    onClick = {},
    flag = FlagIcons.IO,
  )
}