package com.cjproductions.vicinity.search.presentation.filter.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.width
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowWidthSizeClass
import com.cjproductions.vicinity.core.domain.ClassificationName
import com.cjproductions.vicinity.core.domain.ClassificationName.Miscellaneous
import com.cjproductions.vicinity.core.domain.ClassificationName.Sports
import com.cjproductions.vicinity.core.presentation.ui.FILTER_GROUPS
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.XSmall
import com.cjproductions.vicinity.core.presentation.ui.components.RadiusFilterChip
import com.cjproductions.vicinity.core.presentation.ui.theme.VicinityTheme

@Composable
fun ClassificationGroup(
  selectedClassificationNames: List<ClassificationName>,
  onClassificationSelected: (ClassificationName) -> Unit,
  modifier: Modifier = Modifier,
) {
  FlowRow(
    horizontalArrangement = Arrangement.spacedBy(
      space = XSmall.dp,
      alignment = Alignment.CenterHorizontally
        .takeIf {
          currentWindowAdaptiveInfo()
            .windowSizeClass.windowWidthSizeClass != WindowWidthSizeClass.COMPACT
        } ?: Alignment.Start,
    ),
    verticalArrangement = Arrangement.Center,
    itemVerticalAlignment = Alignment.CenterVertically,
    modifier = modifier
  ) {
    FILTER_GROUPS.forEach { classificationGroup ->
      with(classificationGroup) {
        RadiusFilterChip(
          label = title.asString(),
          selected = selectedClassificationNames.contains(classificationName),
          onClick = { onClassificationSelected(classificationName) }
        )
      }
    }
  }
}

@Preview
@Composable
fun ClassificationGroupPreview() {
  VicinityTheme {
    ClassificationGroup(
      selectedClassificationNames = listOf(Miscellaneous, Sports),
      onClassificationSelected = {},
      modifier = Modifier.width(200.dp)
    )
  }
}