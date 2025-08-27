package com.cjproductions.vicinity.core.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize

@Composable
fun RadiusVerticalGrid(
  verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(GlobalPaddingAndSize.Medium.dp),
  horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(0.dp),
  contentPadding: PaddingValues = PaddingValues(bottom = GlobalPaddingAndSize.Medium.dp),
  modifier: Modifier = Modifier,
  content: LazyGridScope.() -> Unit,
) {
  LazyVerticalGrid(
    columns = GridCells.Adaptive(BOTTOM_SHEET_HEIGHT),
    verticalArrangement = verticalArrangement,
    horizontalArrangement = horizontalArrangement,
    contentPadding = contentPadding,
    content = content,
    modifier = modifier,
  )
}