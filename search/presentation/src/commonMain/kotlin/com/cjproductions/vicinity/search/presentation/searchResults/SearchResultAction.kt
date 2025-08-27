package com.cjproductions.vicinity.search.presentation.searchResults

sealed interface SearchResultAction {
  data class OnEventClick(val title: String): SearchResultAction
  data object OnBackClick: SearchResultAction
  data object OnSearchClick: SearchResultAction
  data object OnFilterClick: SearchResultAction
  data class OnShareClick(val title: String): SearchResultAction
  data class OnToggleLikeClick(
    val normalizedTitle: String,
    val title: String,
    val category: String,
    val isLiked: Boolean,
    val image: String?,
    val earliestTimeStamp: Double?,
    val latestTimeStamp: Double?,
  ): SearchResultAction
}