package com.cjproductions.vicinity.core.presentation.ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.Large
import com.cjproductions.vicinity.core.presentation.ui.theme.VicinityTheme
import com.cjproductions.vicinity.likes.domain.LikeState.Show
import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.Heart
import compose.icons.lineawesomeicons.HeartSolid

@Composable
fun LikeButton(
  likeState: Show,
  onLikeClick: () -> Unit,
  buttonColor: Color = MaterialTheme.colorScheme.background,
  modifier: Modifier = Modifier,
) {
  IconToggleButton(
    checked = likeState.liked,
    onCheckedChange = { onLikeClick() },
    modifier = modifier.size(Large.dp),
  ) {
    Icon(
      imageVector = LineAwesomeIcons.Heart.takeUnless { likeState.liked }
        ?: LineAwesomeIcons.HeartSolid,
      contentDescription = null,
      tint = buttonColor,
    )
  }
}

@Preview
@Composable
fun LikeButtonPreview() {
  VicinityTheme {
    Row {
      LikeButton(
        likeState = Show(true),
        onLikeClick = {},
      )

      LikeButton(
        likeState = Show(false),
        onLikeClick = {},
      )
    }
  }
}