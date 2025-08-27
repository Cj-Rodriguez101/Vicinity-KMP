package com.cjproductions.vicinity.geoEvents.presentation.locationSelector

sealed interface LocationSelectorAction {
  data object OnBackClicked: LocationSelectorAction
  data object OnCurrentLocationClicked: LocationSelectorAction
  data class OnSearchLocationChanged(val searchLocation: String): LocationSelectorAction
  data class OnSearchItemClicked(val searchItem: SearchItem): LocationSelectorAction
}