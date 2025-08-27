package com.cjproductions.vicinity.search.presentation.filter

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cjproductions.vicinity.core.domain.ClassificationName
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.XSmall
import com.cjproductions.vicinity.core.presentation.ui.components.HeaderContent
import com.cjproductions.vicinity.core.presentation.ui.components.RadiusButton
import com.cjproductions.vicinity.core.presentation.ui.components.TopNavBarWithBack
import com.cjproductions.vicinity.core.presentation.ui.observeAsEvents
import com.cjproductions.vicinity.core.presentation.ui.theme.VicinityTheme
import com.cjproductions.vicinity.search.presentation.filter.FilterDestination.Back
import com.cjproductions.vicinity.search.presentation.filter.FilterDestination.DatePicker
import com.cjproductions.vicinity.search.presentation.filter.FilterViewAction.OnApplyFilters
import com.cjproductions.vicinity.search.presentation.filter.FilterViewAction.OnGoBackClicked
import com.cjproductions.vicinity.search.presentation.filter.FilterViewAction.OnResetFilters
import com.cjproductions.vicinity.search.presentation.filter.FilterViewAction.OnToggleSortOrder
import com.cjproductions.vicinity.search.presentation.filter.FilterViewAction.OnUpdateClassification
import com.cjproductions.vicinity.search.presentation.filter.FilterViewAction.OnUpdateDateType
import com.cjproductions.vicinity.search.presentation.filter.FilterViewAction.OnUpdateSortOption
import com.cjproductions.vicinity.search.presentation.filter.components.ClassificationGroup
import com.cjproductions.vicinity.search.presentation.filter.components.DateGroup
import com.cjproductions.vicinity.search.presentation.filter.components.SortGroup
import com.cjproductions.vicinity.search.presentation.filter.components.TextSwitch
import com.cjproductions.vicinity.support.search.DateType
import org.jetbrains.compose.resources.stringResource
import vicinity.search.presentation.generated.resources.Res
import vicinity.search.presentation.generated.resources.apply
import vicinity.search.presentation.generated.resources.ascending
import vicinity.search.presentation.generated.resources.classification
import vicinity.search.presentation.generated.resources.date
import vicinity.search.presentation.generated.resources.descending
import vicinity.search.presentation.generated.resources.filters
import vicinity.search.presentation.generated.resources.reset
import vicinity.search.presentation.generated.resources.sort

@Composable
fun FilterScreenRoot(
  viewModel: FilterViewModel,
  onGoToDateFilter: (List<Double>) -> Unit,
  onBackClick: () -> Unit,
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  viewModel.destination.observeAsEvents { event ->
    when (event) {
      is DatePicker -> onGoToDateFilter(event.dates)
      Back -> onBackClick()
    }
  }
  FilterScreen(
    uiState = uiState,
    onFilterAction = viewModel::onAction,
  )
}

@Composable
private fun FilterScreen(
  uiState: FilterViewState,
  onFilterAction: (FilterViewAction) -> Unit,
) {
  VicinityTheme {
    Scaffold(
      contentWindowInsets = WindowInsets.safeDrawing,
      topBar = {
        TopNavBarWithBack(
          title = stringResource(Res.string.filters),
          onBackClick = { onFilterAction(OnGoBackClicked) }
        )
      }
    ) { padding ->
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
          .fillMaxSize()
          .padding(padding)
      ) {
        LazyColumn(
          modifier = Modifier.weight(1f),
          verticalArrangement = Arrangement.spacedBy(XSmall.dp),
          contentPadding = PaddingValues(horizontal = GlobalPaddingAndSize.Medium.dp)
        ) {
          item {
            HeaderContent(
              text = stringResource(Res.string.date),
              modifier = Modifier.padding(bottom = XSmall.dp),
            )
            DateGroup(
              selectedDateType = uiState.dateType,
              customDates = uiState.selectedDates,
              onDateTypeSelected = { dateType, dates ->
                onFilterAction(OnUpdateDateType(dateType, dates))
              },
              modifier = Modifier.fillMaxWidth()
            )
          }

          item {
            HeaderContent(
              text = stringResource(Res.string.classification),
              modifier = Modifier.padding(
                top = GlobalPaddingAndSize.Medium.dp,
                bottom = XSmall.dp,
              ),
            )
            ClassificationGroup(
              selectedClassificationNames = uiState.classificationIds,
              onClassificationSelected = { classificationId ->
                onFilterAction(OnUpdateClassification(classificationId))
              },
              modifier = Modifier.fillMaxWidth()
            )
          }

          item {
            HeaderContent(
              text = stringResource(Res.string.sort),
              modifier = Modifier.padding(
                top = XSmall.dp,
                bottom = XSmall.dp
              ),
            )
            SortGroup(
              selectedSortingOption = uiState.sortType.sortingOption,
              onSortingOptionSelected = { sortOption ->
                onFilterAction(OnUpdateSortOption(sortOption))
              },
              modifier = Modifier.fillMaxWidth()
            )
          }

          item {
            Spacer(modifier = Modifier.height(XSmall.dp))
            TextSwitch(
              options = listOf(
                stringResource(Res.string.ascending),
                stringResource(Res.string.descending),
              ),
              selectedOption = stringResource(Res.string.ascending)
                .takeIf { uiState.sortType.isAscending }
                ?: stringResource(Res.string.descending),
              onOptionSelected = { option ->
                onFilterAction(OnToggleSortOrder)
              },
              modifier = Modifier.fillMaxWidth()
            )
          }
        }
        Row(
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier
            .fillMaxWidth()
            .padding(
              horizontal = GlobalPaddingAndSize.Medium.dp,
              vertical = GlobalPaddingAndSize.Medium.dp,
            )
        ) {
          RadiusButton(
            label = stringResource(Res.string.reset),
            onClick = { onFilterAction(OnResetFilters) },
            modifier = Modifier.weight(0.7f),
            colors = ButtonDefaults.buttonColors(
              containerColor = MaterialTheme.colorScheme.surface,
              contentColor = MaterialTheme.colorScheme.onSurface,
            ),
          )

          Spacer(modifier = Modifier.weight(1f))

          RadiusButton(
            label = stringResource(Res.string.apply),
            enabled = uiState.isApplyButtonEnabled,
            onClick = { onFilterAction(OnApplyFilters) },
            modifier = Modifier.weight(1f),
          )
        }
      }
    }
  }
}

@Preview
@Composable
fun FilterScreenPreview() {
  VicinityTheme {
    Column(
      modifier = Modifier.scrollable(
        state = rememberScrollState(),
        orientation = Orientation.Vertical,
      )
    ) {
      FilterScreen(
        uiState = FilterViewState(
          dateType = DateType.Today,
          classificationIds = listOf(
            ClassificationName.Miscellaneous,
            ClassificationName.Sports,
          )
        ),
        onFilterAction = {}
      )
    }
  }
}