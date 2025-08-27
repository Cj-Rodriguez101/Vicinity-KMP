package com.cjproductions.vicinity.discover.presentation.discover

sealed interface DiscoverDestination {
  data object RefreshEvents: DiscoverDestination
  data object SearchEvents: DiscoverDestination
  data object Filter: DiscoverDestination
  data class DestinationDetail(val title: String): DiscoverDestination
}