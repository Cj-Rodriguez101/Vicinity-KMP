package com.cjproductions.vicinity.search.presentation.searchResults

sealed interface SearchResultDestination {
  data object Back: SearchResultDestination
  data object Search: SearchResultDestination
  data object Filter: SearchResultDestination
}