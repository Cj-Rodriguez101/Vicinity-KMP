@file:OptIn(ExperimentalResourceApi::class)

package com.cjproductions.vicinity.core.presentation.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.Font
import vicinity.core.presentation.ui.generated.resources.Mulish_Bold
import vicinity.core.presentation.ui.generated.resources.Mulish_Light
import vicinity.core.presentation.ui.generated.resources.Mulish_Medium
import vicinity.core.presentation.ui.generated.resources.Mulish_Regular
import vicinity.core.presentation.ui.generated.resources.Mulish_SemiBold
import vicinity.core.presentation.ui.generated.resources.Res

@Composable
fun MullinsFontFamily() = FontFamily(
  Font(Res.font.Mulish_Light, weight = FontWeight.Light),
  Font(Res.font.Mulish_Regular, weight = FontWeight.Normal),
  Font(Res.font.Mulish_Medium, weight = FontWeight.Medium),
  Font(Res.font.Mulish_SemiBold, weight = FontWeight.SemiBold),
  Font(Res.font.Mulish_Bold, weight = FontWeight.Bold)
)

@Composable
fun MullinsTypography() = Typography(
  displayLarge = TextStyle(
    fontFamily = MullinsFontFamily(),
    fontSize = 57.sp,
    fontWeight = FontWeight.Normal,
    lineHeight = 64.sp,
    letterSpacing = (-0.25).sp
  ),
  displayMedium = TextStyle(
    fontFamily = MullinsFontFamily(),
    fontSize = 45.sp,
    fontWeight = FontWeight.Normal,
    lineHeight = 52.sp,
    letterSpacing = 0.sp
  ),
  displaySmall = TextStyle(
    fontFamily = MullinsFontFamily(),
    fontSize = 36.sp,
    fontWeight = FontWeight.Normal,
    lineHeight = 44.sp,
    letterSpacing = 0.sp
  ),
  headlineLarge = TextStyle(
    fontFamily = MullinsFontFamily(),
    fontSize = 32.sp,
    fontWeight = FontWeight.Bold,
    lineHeight = 40.sp,
    letterSpacing = 0.sp
  ),
  headlineMedium = TextStyle(
    fontFamily = MullinsFontFamily(),
    fontSize = 28.sp,
    fontWeight = FontWeight.SemiBold,
    lineHeight = 36.sp,
    letterSpacing = 0.sp
  ),
  headlineSmall = TextStyle(
    fontFamily = MullinsFontFamily(),
    fontSize = 24.sp,
    fontWeight = FontWeight.SemiBold,
    lineHeight = 32.sp,
    letterSpacing = 0.sp
  ),
  titleLarge = TextStyle(
    fontFamily = MullinsFontFamily(),
    fontSize = 22.sp,
    fontWeight = FontWeight.Medium,
    lineHeight = 28.sp,
    letterSpacing = 0.sp
  ),
  titleMedium = TextStyle(
    fontFamily = MullinsFontFamily(),
    fontSize = 16.sp,
    fontWeight = FontWeight.Medium,
    lineHeight = 24.sp,
    letterSpacing = 0.15.sp
  ),
  titleSmall = TextStyle(
    fontFamily = MullinsFontFamily(),
    fontSize = 14.sp,
    fontWeight = FontWeight.Medium,
    lineHeight = 20.sp,
    letterSpacing = 0.1.sp
  ),
  bodyLarge = TextStyle(
    fontFamily = MullinsFontFamily(),
    fontSize = 16.sp,
    fontWeight = FontWeight.Normal,
    lineHeight = 24.sp,
    letterSpacing = 0.5.sp
  ),
  bodyMedium = TextStyle(
    fontFamily = MullinsFontFamily(),
    fontSize = 14.sp,
    fontWeight = FontWeight.Normal,
    lineHeight = 20.sp,
    letterSpacing = 0.25.sp
  ),
  bodySmall = TextStyle(
    fontFamily = MullinsFontFamily(),
    fontSize = 12.sp,
    fontWeight = FontWeight.Normal,
    lineHeight = 16.sp,
    letterSpacing = 0.4.sp
  ),
  labelLarge = TextStyle(
    fontFamily = MullinsFontFamily(),
    fontSize = 14.sp,
    fontWeight = FontWeight.Medium,
    lineHeight = 20.sp,
    letterSpacing = 0.1.sp
  ),
  labelMedium = TextStyle(
    fontFamily = MullinsFontFamily(),
    fontSize = 12.sp,
    fontWeight = FontWeight.Medium,
    lineHeight = 16.sp,
    letterSpacing = 0.5.sp
  ),
  labelSmall = TextStyle(
    fontFamily = MullinsFontFamily(),
    fontSize = 11.sp,
    fontWeight = FontWeight.Medium,
    lineHeight = 16.sp,
    letterSpacing = 0.5.sp
  )
)