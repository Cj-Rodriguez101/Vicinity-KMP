package com.cjproductions.vicinity.search.presentation.filter.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize
import com.cjproductions.vicinity.core.presentation.ui.components.RadiusCard
import com.cjproductions.vicinity.core.presentation.ui.theme.LightGray

@Composable
fun RadiusSelectContainer(
  modifier: Modifier = Modifier,
  title: String,
  selected: Boolean = false,
  onClick: () -> Unit,
  selectContainer: @Composable () -> Unit,
) {
  RadiusCard(
    backgroundColor = if (selected) LightGray else MaterialTheme.colorScheme.surface,
    modifier = modifier.clickable { onClick() },
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween,
      modifier = Modifier.padding(horizontal = GlobalPaddingAndSize.Medium.dp)
    ) {
      Text(
        text = title,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.weight(1f),
      )

      selectContainer()
    }
  }
}