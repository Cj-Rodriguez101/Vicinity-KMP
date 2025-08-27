package com.cjproductions.vicinity.support.share.presentation

import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.Large
import com.cjproductions.vicinity.support.tools.Platform
import com.cjproductions.vicinity.support.tools.PlatformType
import org.jetbrains.compose.resources.vectorResource
import vicinity.support.share.presentation.generated.resources.Res
import vicinity.support.share.presentation.generated.resources.Share_android
import vicinity.support.share.presentation.generated.resources.Share_ios

@Composable
fun ShareButton(
  onClick: () -> Unit,
) {
  IconButton(
    onClick = onClick,
    modifier = Modifier.size(Large.dp),
  ) {
    Icon(
      imageVector = getShareIcon(),
      contentDescription = null,
      tint = Color.Black,
    )
  }
}

@Composable
fun getShareIcon(): ImageVector = when (Platform.type) {
  PlatformType.IOS -> vectorResource(Res.drawable.Share_ios)
  else -> vectorResource(Res.drawable.Share_android)
}