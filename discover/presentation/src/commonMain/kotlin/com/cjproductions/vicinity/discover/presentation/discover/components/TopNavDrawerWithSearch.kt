package com.cjproductions.vicinity.discover.presentation.discover.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize
import com.cjproductions.vicinity.core.presentation.ui.components.FlagCircle
import com.cjproductions.vicinity.core.presentation.ui.components.ScaffoldTopBar
import com.cjproductions.vicinity.core.presentation.ui.components.SearchFilterBar
import com.cjproductions.vicinity.core.presentation.ui.theme.VicinityTheme
import org.jetbrains.compose.resources.stringResource
import vicinity.core.presentation.ui.generated.resources.Res
import vicinity.core.presentation.ui.generated.resources.app_name

@Composable
fun TopNavWithSearchFilter(
  filterCount: Int,
  query: String,
  flagImage: ImageVector?,
  onOpenCountrySelect: () -> Unit,
  onSearchClick: () -> Unit,
  onFilterClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  ScaffoldTopBar(
    title = stringResource(Res.string.app_name),
    trailingContent = {
      Row(modifier = Modifier.padding(end = GlobalPaddingAndSize.Medium.dp)) {
        FlagCircle(
          onClick = onOpenCountrySelect,
          flag = flagImage,
        )
      }
    },
    bottomContent = {
      SearchFilterBar(
        filterCount = filterCount,
        textContent = { Text(text = query) },
        onClick = onSearchClick,
        onFilterClick = onFilterClick,
      )
    }
  )
}

@Preview
@Composable
fun TopNavDrawerWithSearchPreview() {
  VicinityTheme {
    TopNavWithSearchFilter(
      filterCount = 0,
      query = "Search events, venues and more",
      flagImage = null,
      onOpenCountrySelect = {},
      onSearchClick = {},
      onFilterClick = {},
    )
  }
}