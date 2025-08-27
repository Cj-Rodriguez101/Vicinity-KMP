package com.cjproductions.vicinity.core.presentation.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.Large

@Composable
fun RadiusIcon(
  icon: ImageVector,
  contentDescription: String? = null,
) {
  Icon(
    imageVector = icon,
    contentDescription = contentDescription,
    modifier = Modifier.size(Large.dp),
  )
}