package com.cjproductions.vicinity.geoEvents.presentation.globalEvents

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.XSmall
import com.cjproductions.vicinity.core.presentation.ui.components.RadiusCard
import com.cjproductions.vicinity.core.presentation.ui.components.SearchBar
import com.cjproductions.vicinity.core.presentation.ui.theme.VicinityTheme
import com.cjproductions.vicinity.geoEvents.presentation.globalEvents.GlobalEventAction.OnCompassClick
import com.cjproductions.vicinity.geoEvents.presentation.globalEvents.GlobalEventViewState.Loaded
import com.cjproductions.vicinity.geoEvents.presentation.globalEvents.GlobalEventViewState.Loading
import com.cjproductions.vicinity.geoEvents.presentation.globalEvents.GlobalEventViewState.NoLocationPermission
import com.cjproductions.vicinity.geoEvents.presentation.globalEvents.components.EventMapUI
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import ovh.plrapps.mapcompose.ui.state.MapState
import vicinity.geoevents.presentation.generated.resources.Res
import vicinity.geoevents.presentation.generated.resources.no_location_permission
import vicinity.geoevents.presentation.generated.resources.search_location
import vicinity.geoevents.presentation.generated.resources.total_events

@Composable
fun GlobalEventScreenRoot(
  viewModel: GlobalEventViewModel = koinViewModel(),
  onSearchBarClick: () -> Unit,
  onCallOutClick: (eventName: String) -> Unit,
) {
  val uiState by viewModel.state.collectAsStateWithLifecycle()
  GlobalEventScreen(
    uiState = uiState,
    onSearchBarClick = onSearchBarClick,
    onAction = viewModel::onAction,
    onCallOutClick = onCallOutClick,
  )
}

@Composable
private fun GlobalEventScreen(
  uiState: GlobalEventViewState,
  onSearchBarClick: () -> Unit,
  onAction: (GlobalEventAction) -> Unit,
  onCallOutClick: (eventName: String) -> Unit,
) {
  VicinityTheme {
    Scaffold { padding ->
      val layoutDirection = LocalLayoutDirection.current
      Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize().padding(
          start = padding.calculateStartPadding(layoutDirection),
          end = padding.calculateEndPadding(layoutDirection),
        )
      ) {
        when (uiState) {
          is Loaded -> {
            uiState.selectedLocation?.let { location ->
              EventMapUI(
                uiState = uiState,
                onCompassClick = { onAction(OnCompassClick) },
                onCallOutClick = { eventName -> onCallOutClick(eventName) },
                modifier = Modifier.fillMaxSize(),
              )
            }

            Column(
              verticalArrangement = Arrangement.SpaceBetween,
              modifier = Modifier
                .fillMaxSize()
                .align(Alignment.TopCenter)
                .padding(
                  top = padding.calculateTopPadding(),
                  bottom = padding.calculateBottomPadding(),
                )
                .padding(
                  top = XSmall.dp,
                  start = GlobalPaddingAndSize.Medium.dp,
                  end = GlobalPaddingAndSize.Medium.dp,
                  bottom = XSmall.dp,
                ).displayCutoutPadding(),
            ) {
              SearchBar(
                textContent = {
                  Text(
                    text = uiState.selectedLocation?.country ?: stringResource(
                      Res.string.search_location
                    )
                  )
                },
                onClick = onSearchBarClick,
              )

              if (uiState.totalEvents > 0) {
                RadiusCard {
                  Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                      .fillMaxWidth()
                      .padding(GlobalPaddingAndSize.Medium.dp)
                  ) {
                    Text(text = stringResource(Res.string.total_events))
                    Text(
                      text = uiState.totalEvents.toString(),
                      fontWeight = FontWeight.Black,
                    )
                  }
                }
              }
            }
          }

          Loading -> CircularProgressIndicator()
          NoLocationPermission -> Text(stringResource(Res.string.no_location_permission))
        }

      }
    }
  }
}

@Preview
@Composable
private fun GlobalEventScreenPreview() {
  VicinityTheme {
    GlobalEventScreen(
      uiState = Loaded(
        mapState = MapState(
          levelCount = 16,
          fullWidth = 256,
          fullHeight = 256,
          workerCount = 16
        )
      ),
      onSearchBarClick = {},
      onCallOutClick = {},
      onAction = {}
    )
  }
}