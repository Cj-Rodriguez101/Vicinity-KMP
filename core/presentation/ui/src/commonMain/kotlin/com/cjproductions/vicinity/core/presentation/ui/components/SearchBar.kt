package com.cjproductions.vicinity.core.presentation.ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.XXXLarge
import com.cjproductions.vicinity.core.presentation.ui.theme.VicinityTheme
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Search

@Composable
fun SearchBar(
  textContent: @Composable () -> Unit,
  trailingContent: (@Composable () -> Unit)? = null,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Row(
    horizontalArrangement = Arrangement.spacedBy(GlobalPaddingAndSize.Medium.dp),
    verticalAlignment = Alignment.CenterVertically,
    modifier = modifier
      .fillMaxWidth()
      .height(XXXLarge.dp)
      .clickable(onClick = onClick)
      .background(
        color = MaterialTheme.colorScheme.surface,
        shape = MaterialTheme.shapes.medium,
      ).padding(horizontal = GlobalPaddingAndSize.Medium.dp)
  ) {
    Icon(
      imageVector = FontAwesomeIcons.Solid.Search,
      contentDescription = null,
      modifier = Modifier.size(GlobalPaddingAndSize.Medium.dp),
    )

    textContent()
    trailingContent?.let {
      Spacer(modifier = Modifier.weight(1f))
      trailingContent()
    }
  }
}

@Preview
@Composable
fun SearchBarPreview() {
  VicinityTheme {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .height(XXXLarge.dp)
    ) {
      SearchBar(
        textContent = {
          Text(text = "Search")
        },
        onClick = {}
      )
    }
  }
}