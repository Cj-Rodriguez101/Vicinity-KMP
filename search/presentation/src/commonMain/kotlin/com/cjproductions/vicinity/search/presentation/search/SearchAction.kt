package com.cjproductions.vicinity.search.presentation.search

import com.cjproductions.vicinity.search.domain.model.RecentSearch

sealed interface SearchAction {
  data class OnEventSearchChanged(val eventSearch: String): SearchAction
  data class OnLocationSearchChanged(val locationSearch: String): SearchAction
  data object OnClearEventSearch: SearchAction
  data object OnClearLocationSearch: SearchAction
  data object OnSearchClicked: SearchAction
  data object OnBackClick: SearchAction
  data class OnSearchItemClicked(val searchItem: RecentSearch): SearchAction
  data class OnDeleteSearchItem(val searchTerm: RecentSearch): SearchAction
}