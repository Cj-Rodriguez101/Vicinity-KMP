package com.cjproductions.vicinity.search.presentation.search

import com.cjproductions.vicinity.search.domain.model.RecentSearch

data class SearchViewState(
  val eventSearch: FormField = FormField(),
  val locationSearch: FormField = FormField(),
  val isSearchButtonEnabled: Boolean = false,
  val isSearchButtonLoading: Boolean = false,
  val recentSearches: List<RecentSearch> = listOf(),
  val currentSearches: List<CurrentSearch> = listOf(),
)