package com.cjproductions.vicinity.search.presentation.filter

import com.cjproductions.vicinity.core.domain.ClassificationName
import com.cjproductions.vicinity.support.search.DateType
import com.cjproductions.vicinity.support.search.SortingOption
import kotlinx.datetime.LocalDateTime

sealed interface FilterViewAction {
  data object OnGoBackClicked: FilterViewAction
  data class OnUpdateDateType(
    val dateType: DateType,
    val dates: List<LocalDateTime>? = null,
  ): FilterViewAction

  data class OnUpdateSortOption(val sortingOption: SortingOption): FilterViewAction
  data object OnToggleSortOrder: FilterViewAction
  data class OnUpdateClassification(val classificationName: ClassificationName): FilterViewAction
  data object OnApplyFilters: FilterViewAction
  data object OnResetFilters: FilterViewAction
}