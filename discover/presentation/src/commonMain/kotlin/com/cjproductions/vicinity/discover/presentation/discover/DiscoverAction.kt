package com.cjproductions.vicinity.discover.presentation.discover

sealed interface DiscoverAction {
  data object OnBackClick: DiscoverAction
  data object OnSearchClick: DiscoverAction
  data object OnFilterClick: DiscoverAction
  data class OnShareClick(val title: String): DiscoverAction
  data class OnSelectClassification(val name: String): DiscoverAction
  data class OnEventClick(val title: String): DiscoverAction
  data class OnToggleEventLike(
    val normalizedTitle: String,
    val title: String,
    val category: String,
    val isLiked: Boolean,
    val image: String?,
    val startTimeStamp: Double?,
    val endTimeStamp: Double?,
  ): DiscoverAction
}