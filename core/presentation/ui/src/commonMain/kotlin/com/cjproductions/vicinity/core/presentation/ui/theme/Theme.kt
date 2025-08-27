package com.cjproductions.vicinity.core.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

@Composable
fun VicinityTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit
) {
  val colorScheme = LightColorPalette

  MaterialTheme(
    colorScheme = colorScheme,
    typography = MullinsTypography(),
    content = content,
    shapes = VicinityShapes,
  )
}

private val LightColorPalette = lightColorScheme(
  primary = Blue600,
  primaryContainer = Blue800,
  secondary = BlueGray600,
  secondaryContainer = BlueGray700,
  background = OffWhite,
  surface = White,
  error = Red600,
  onPrimary = White,
  onSecondary = White,
  onBackground = AlmostBlack,
  onSurface = AlmostBlack,
  onError = White
)

private val DarkColorPalette = darkColorScheme(
  primary = Blue200,
  primaryContainer = Blue600,
  secondary = BlueGray200,
  secondaryContainer = BlueGray600,
  background = DarkBackground,
  surface = AlmostBlack,
  error = Red200,
  onPrimary = Blue900,
  onSecondary = BlueGray800,
  onBackground = LightGray,
  onSurface = LightGray,
  onError = Red800
)
