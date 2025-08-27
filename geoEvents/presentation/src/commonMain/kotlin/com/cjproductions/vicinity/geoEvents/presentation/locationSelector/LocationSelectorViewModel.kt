package com.cjproductions.vicinity.geoEvents.presentation.locationSelector

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cjproductions.vicinity.geoEvents.presentation.locationSelector.LocationSelectorAction.OnBackClicked
import com.cjproductions.vicinity.geoEvents.presentation.locationSelector.LocationSelectorAction.OnCurrentLocationClicked
import com.cjproductions.vicinity.geoEvents.presentation.locationSelector.LocationSelectorAction.OnSearchItemClicked
import com.cjproductions.vicinity.geoEvents.presentation.locationSelector.LocationSelectorAction.OnSearchLocationChanged
import com.cjproductions.vicinity.location.domain.LocationRepository
import com.cjproductions.vicinity.location.domain.model.UserSetLocation
import com.cjproductions.vicinity.location.domain.model.getPlaceName
import com.cjproductions.vicinity.location.domain.model.toUserSetLocation
import com.cjproductions.vicinity.search.domain.SearchRepository
import com.cjproductions.vicinity.support.tools.Debouncer
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LocationSelectorViewModel(
  private val locationRepository: LocationRepository,
  private val searchRepository: SearchRepository,
  private val deBouncer: Debouncer,
): ViewModel() {
  private val _destination = Channel<Unit>()
  val destination = _destination.receiveAsFlow()

  private val userLocation = locationRepository.location

  private val _uiState = MutableStateFlow(
    LocationSelectorState(
      location = userLocation.value?.let {
        if (it.isUserSet)
          it.userSetLocation?.getPlaceName() else it.defaultLocation?.getPlaceName()
      },
      searches = emptyList(),
    )
  )

  val uiState = _uiState.asStateFlow()

  fun onAction(action: LocationSelectorAction) {
    when (action) {
      is OnSearchLocationChanged -> updateLocation(action.searchLocation)

      is OnSearchItemClicked -> setSearchedLocation(action.searchItem)

      OnCurrentLocationClicked -> updateUserLocation()

      OnBackClicked -> goBack()
    }
  }

  private fun setSearchedLocation(searchItem: SearchItem) {
    viewModelScope.launch {
      locationRepository.updateUserSetLocation(
        with(searchItem) {
          UserSetLocation(
            latitude = latitude,
            longitude = longitude,
            countryCode = countryCode,
            country = discoveredCountry,
            city = discoveredCountry,
          )
        }
      )
      _uiState.update { it.copy(location = searchItem.discoveredCountry) }
      _destination.send(Unit)
    }
  }

  private fun updateUserLocation() {
    viewModelScope.launch {
      _uiState.update { it.copy(loading = true) }
      locationRepository.getLocation().getOrNull()?.let { location ->
        locationRepository.updateDefaultLocation(location)
        locationRepository.updateUserSetLocation(location.toUserSetLocation())
        _uiState.update { it.copy(location = location.getPlaceName()) }
        _destination.send(Unit)
      }
      _uiState.update { it.copy(loading = false) }
    }
  }

  private fun goBack() {
    viewModelScope.launch { _destination.send(Unit) }
  }

  private fun updateLocation(location: String) {
    _uiState.update { it.copy(searchLocation = location) }
    if (location.length < 3) return
    deBouncer.debounceAction(scope = viewModelScope) {
      val locations = searchRepository.fetchLocations(location)
      locations.getOrNull()?.let { locations ->
        _uiState.update { locationState ->
          locationState.copy(
            searches = locations.toSearchItems()
              .distinctBy { it.discoveredCountry })
        }
      }
    }
  }
}