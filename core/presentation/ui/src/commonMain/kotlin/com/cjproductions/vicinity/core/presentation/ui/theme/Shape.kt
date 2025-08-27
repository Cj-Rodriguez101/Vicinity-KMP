package com.cjproductions.vicinity.core.presentation.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.XSmall

val SMALL = RoundedCornerShape(XSmall.dp)
val MEDIUM = RoundedCornerShape(GlobalPaddingAndSize.Medium.dp)
val LARGE = RoundedCornerShape(GlobalPaddingAndSize.Large.dp)

val VicinityShapes = Shapes(
  small = SMALL,
  medium = MEDIUM,
  large = LARGE,
)