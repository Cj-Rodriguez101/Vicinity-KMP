package com.cjproductions.vicinity.search.presentation.search

sealed interface SearchDestination {
  data object SearchResult: SearchDestination
  data object Back: SearchDestination
}