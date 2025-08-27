package com.cjproductions.vicinity.search.presentation.filter.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.XSmall
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.XXXLarge
import com.cjproductions.vicinity.core.presentation.ui.theme.VicinityTheme

@Composable
fun TextSwitch(
  options: List<String>,
  selectedOption: String,
  onOptionSelected: (String) -> Unit,
  modifier: Modifier = Modifier,
) {
  Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceAround,
  ) {
    options.forEachIndexed { i, option ->
      SwitchItem(
        text = option,
        selected = option == selectedOption,
        onClick = {
          onOptionSelected(option)
        }
      )
    }
  }
}

@Composable
private fun RowScope.SwitchItem(
  text: String,
  selected: Boolean,
  onClick: () -> Unit,
) {
  Box(
    modifier = Modifier
      .weight(1f)
      .background(
        color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
        shape = MaterialTheme.shapes.small,
      )
      .clickable(onClick = onClick),
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = text,
      style = MaterialTheme.typography.bodyMedium,
      textAlign = TextAlign.Center,
      fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
      color = if (selected) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onSurface,
      modifier = Modifier
        .fillMaxHeight()
        .padding(XSmall.dp)
    )
  }
}

@Preview
@Composable
fun TextSwitchPreview() {
  VicinityTheme {
    TextSwitch(
      options = listOf("Option 1", "Option 2", "Option 3"),
      selectedOption = "Option 2",
      onOptionSelected = {},
      modifier = Modifier.height(XXXLarge.dp)
    )
  }
}