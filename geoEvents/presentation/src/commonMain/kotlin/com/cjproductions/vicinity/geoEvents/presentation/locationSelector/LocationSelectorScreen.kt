package com.cjproductions.vicinity.geoEvents.presentation.locationSelector

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.XSmall
import com.cjproductions.vicinity.core.presentation.ui.components.DefaultLabeledIcon
import com.cjproductions.vicinity.core.presentation.ui.components.RadiusButton
import com.cjproductions.vicinity.core.presentation.ui.components.RadiusTextField
import com.cjproductions.vicinity.core.presentation.ui.components.TopNavBarWithBack
import com.cjproductions.vicinity.core.presentation.ui.observeAsEvents
import com.cjproductions.vicinity.core.presentation.ui.theme.VicinityTheme
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.MapMarkerAlt
import compose.icons.fontawesomeicons.solid.Search
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import vicinity.geoevents.presentation.generated.resources.Res
import vicinity.geoevents.presentation.generated.resources.location
import vicinity.geoevents.presentation.generated.resources.not_set
import vicinity.geoevents.presentation.generated.resources.search_location
import vicinity.geoevents.presentation.generated.resources.use_current_location

@Composable
fun LocationSelectorScreenRoot(
  viewModel: LocationSelectorViewModel = koinViewModel(),
  onBack: () -> Unit,
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  viewModel.destination.observeAsEvents { onBack() }
  LocationSelectorScreen(
    uiState = uiState,
    onAction = viewModel::onAction,
  )
}

@Composable
private fun LocationSelectorScreen(
  uiState: LocationSelectorState,
  onAction: (LocationSelectorAction) -> Unit,
) {
  VicinityTheme {
    Scaffold(
      topBar = {
        TopNavBarWithBack(
          title = stringResource(Res.string.location),
          onBackClick = { onAction(LocationSelectorAction.OnBackClicked) },
        )
      }
    ) { padding ->
      val listState = rememberLazyListState()
      Column(
        verticalArrangement = Arrangement.spacedBy(GlobalPaddingAndSize.Medium.dp),
        modifier = Modifier
          .scrollable(
            state = listState,
            orientation = Orientation.Vertical,
          )
          .padding(padding)
          .padding(
            start = GlobalPaddingAndSize.Medium.dp,
            end = GlobalPaddingAndSize.Medium.dp,
          ).fillMaxSize()
      ) {

        Row(
          horizontalArrangement = Arrangement.spacedBy(GlobalPaddingAndSize.Medium.dp),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Box(
            modifier = Modifier.background(
              color = MaterialTheme.colorScheme.surface,
              shape = MaterialTheme.shapes.small,
            ).padding(XSmall.dp)
          ) {
            Icon(
              imageVector = FontAwesomeIcons.Solid.MapMarkerAlt,
              contentDescription = null,
              modifier = Modifier.size(GlobalPaddingAndSize.Medium.dp)
            )
          }

          Text(
            text = uiState.location ?: stringResource(Res.string.not_set),
            style = MaterialTheme.typography.titleMedium,
          )
        }

        RadiusButton(
          label = stringResource(Res.string.use_current_location),
          loading = uiState.loading,
        ) {
          onAction(LocationSelectorAction.OnCurrentLocationClicked)
        }

        RadiusTextField(
          text = uiState.searchLocation.orEmpty(),
          hint = stringResource(Res.string.search_location),
          onTextChange = { onAction(LocationSelectorAction.OnSearchLocationChanged(it)) },
        )

        LazyColumn(
          state = listState,
          modifier = Modifier.weight(1f),
        ) {
          items(items = uiState.searches) { search ->
            DefaultLabeledIcon(
              leadingIcon = FontAwesomeIcons.Solid.Search,
              text = search.discoveredCountry,
              containerModifier = Modifier.fillMaxWidth().clickable {
                onAction(LocationSelectorAction.OnSearchItemClicked(search))
              }.padding(vertical = GlobalPaddingAndSize.Medium.dp),
            )
          }
        }
      }
    }
  }
}

@Preview
@Composable
private fun LocationSelectorScreenPreview() {
  LocationSelectorScreen(
    uiState = LocationSelectorState(
      searchLocation = "editLocation",
      location = "location",
      searches = listOf(
        SearchItem(
          latitude = 0.0,
          longitude = 0.0,
          discoveredCountry = "country",
          countryCode = "code",
        ),
        SearchItem(
          latitude = 0.0,
          longitude = 0.0,
          discoveredCountry = "country",
          countryCode = "code",
        )
      ),
    ),
    onAction = {},
  )
}