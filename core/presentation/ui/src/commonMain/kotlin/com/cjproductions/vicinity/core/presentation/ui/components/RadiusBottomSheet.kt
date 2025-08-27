package com.cjproductions.vicinity.core.presentation.ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize
import kotlinx.coroutines.launch

@Composable
fun RadiusBottomSheet(
  onDismiss: () -> Unit,
  modifier: Modifier = Modifier,
  content: @Composable (onDismiss: () -> Unit) -> Unit,
) {
  val bottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Expanded)

  val scope = rememberCoroutineScope()

  LaunchedEffect(bottomSheetState.currentValue) {
    if (bottomSheetState.currentValue == ModalBottomSheetValue.Hidden) {
      onDismiss()
    }
  }
  ModalBottomSheetLayout(
    sheetState = bottomSheetState,
    sheetContentColor = MaterialTheme.colorScheme.background,
    sheetShape = RoundedCornerShape(
      topStart = GlobalPaddingAndSize.Medium.dp,
      topEnd = GlobalPaddingAndSize.Medium.dp
    ),
    sheetElevation = 0.dp,
    scrimColor = Color.Transparent,
    sheetContent = { content { scope.launch { bottomSheetState.hide() } } },
    modifier = modifier,
  ) {}
}

@Preview
@Composable
fun PreviewRadiusBottomSheet() {
  RadiusBottomSheet(
    onDismiss = {},
  ) { onDismiss ->
  }
}