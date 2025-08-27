package com.cjproductions.vicinity.search.presentation.filter.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize
import com.cjproductions.vicinity.core.presentation.ui.theme.VicinityTheme

@Composable
fun SingleSelectItem(
  title: String,
  selected: Boolean = false,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  RadiusSelectContainer(
    title = title,
    selected = selected,
    onClick = onClick,
    modifier = modifier,
    selectContainer = {
      RadioButton(
        selected = selected,
        onClick = onClick
      )
    }
  )
}

@Preview
@Composable
fun SingleSelectItemPreview() {
  VicinityTheme {
    SingleSelectItem(
      title = "Item 1",
      selected = true,
      onClick = {},
      modifier = Modifier.padding(GlobalPaddingAndSize.Medium.dp)
    )
  }
}