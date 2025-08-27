package com.cjproductions.vicinity.search.presentation.filter.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cjproductions.vicinity.core.presentation.designsystem.components.UIText
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.XSmall
import com.cjproductions.vicinity.support.search.SortingOption
import vicinity.search.presentation.generated.resources.Res
import vicinity.search.presentation.generated.resources.date
import vicinity.search.presentation.generated.resources.name
import vicinity.search.presentation.generated.resources.relevance

@Composable
fun SortGroup(
  selectedSortingOption: SortingOption,
  onSortingOptionSelected: (SortingOption) -> Unit,
  modifier: Modifier = Modifier,
) {
  Column(verticalArrangement = Arrangement.spacedBy(XSmall.dp)) {
    SortingOption.entries.forEach { sortingOption ->
      SingleSelectItem(
        title = sortingOption.getString().asString(),
        selected = selectedSortingOption == sortingOption,
        onClick = { onSortingOptionSelected(sortingOption) },
        modifier = modifier,
      )
    }
  }
}

private fun SortingOption.getString(): UIText {
  return when (this) {
    SortingOption.Relevance -> UIText.StringResourceId(Res.string.relevance)
    SortingOption.Date -> UIText.StringResourceId(Res.string.date)
    SortingOption.Name -> UIText.StringResourceId(Res.string.name)
  }
}