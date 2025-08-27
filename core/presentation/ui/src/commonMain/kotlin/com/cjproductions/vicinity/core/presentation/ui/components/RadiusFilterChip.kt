package com.cjproductions.vicinity.core.presentation.ui.components

import androidx.compose.material.LocalTextStyle
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import com.cjproductions.vicinity.core.presentation.ui.theme.LightGray

@Composable
fun RadiusFilterChip(
  label: String,
  selected: Boolean,
  textStyle: TextStyle = LocalTextStyle.current,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  ElevatedFilterChip(
    selected = selected,
    onClick = onClick,
    colors = FilterChipDefaults.filterChipColors(
      containerColor = MaterialTheme.colorScheme.surface,
      selectedContainerColor = LightGray
    ),
    label = {
      Text(
        text = label,
        style = textStyle,
      )
    },
    shape = MaterialTheme.shapes.medium,
    modifier = modifier,
  )
}