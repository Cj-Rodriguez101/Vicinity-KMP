package com.cjproductions.vicinity.discover.presentation.countrySelector

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize
import com.cjproductions.vicinity.core.presentation.ui.components.BOTTOM_SHEET_HEIGHT
import com.cjproductions.vicinity.core.presentation.ui.theme.VicinityTheme
import com.cjproductions.vicinity.discover.presentation.countrySelector.CountrySelectorState.Loaded
import com.cjproductions.vicinity.discover.presentation.countrySelector.CountrySelectorState.Loading
import com.cjproductions.vicinity.discover.presentation.countrySelector.components.CountryCard
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import vicinity.discover.presentation.generated.resources.Res
import vicinity.discover.presentation.generated.resources.choose_country

@Composable
fun CountrySelectorScreenRoot(
  viewModel: CountrySelectorViewModel = koinViewModel(),
  onDismiss: () -> Unit,
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  CountrySelectorScreen(
    uiState = uiState,
    onSetCountry = {
      viewModel.onSetCountry(it)
      onDismiss()
    },
  )
}

@Composable
fun CountrySelectorScreen(
  uiState: CountrySelectorState,
  onSetCountry: (CountryUI) -> Unit,
) {
  VicinityTheme {
    Scaffold(modifier = Modifier.height(BOTTOM_SHEET_HEIGHT)) {
      Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text(
          text = stringResource(Res.string.choose_country),
          color = MaterialTheme.colorScheme.onSurface,
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.Bold,
          modifier = Modifier.padding(GlobalPaddingAndSize.Medium.dp),
        )
        Box(
          contentAlignment = Alignment.Center,
        ) {
          when (uiState) {
            is Loading -> CircularProgressIndicator()
            is Loaded -> {
              val startIndex by remember {
                mutableStateOf(
                  uiState.countries.indexOfFirst { it.countryCode == uiState.selectedCountryCode })
              }
              val state = rememberLazyListState(
                initialFirstVisibleItemIndex = startIndex.coerceIn(
                  minimumValue = 0,
                  maximumValue = Int.MAX_VALUE
                )
              )
              LazyColumn(state = state) {
                items(items = uiState.countries) { country ->
                  CountryCard(
                    selected = country.countryCode == uiState.selectedCountryCode,
                    countryUI = country,
                    onClick = { onSetCountry(country) }
                  )
                }
              }
            }
          }
        }
      }
    }
  }
}