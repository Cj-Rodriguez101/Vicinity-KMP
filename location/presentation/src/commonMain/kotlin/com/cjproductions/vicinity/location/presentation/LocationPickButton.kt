package com.cjproductions.vicinity.location.presentation

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cjproductions.vicinity.core.presentation.ui.components.RoundedButton
import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.Compass

@Composable
fun LocationPickButton(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  RoundedButton(
    icon = LineAwesomeIcons.Compass,
    contentColor = MaterialTheme.colorScheme.primary,
    onClick = onClick,
    modifier = modifier,
  )
}