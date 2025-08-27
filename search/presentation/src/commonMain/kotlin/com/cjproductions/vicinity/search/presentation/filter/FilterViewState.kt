package com.cjproductions.vicinity.search.presentation.filter

import com.cjproductions.vicinity.core.domain.ClassificationName
import com.cjproductions.vicinity.support.search.DateType
import com.cjproductions.vicinity.support.search.FilterParams
import com.cjproductions.vicinity.support.search.SortType
import com.cjproductions.vicinity.support.search.SortingOption
import kotlinx.datetime.LocalDateTime

data class FilterViewState(
  val classificationIds: List<ClassificationName> = listOf(),
  val selectedDates: List<LocalDateTime>? = null,
  val dateType: DateType = DateType.AnyDate,
  val sortType: SortType = SortType(
    sortingOption = SortingOption.Relevance,
    isAscending = true,
  ),
  val isApplyButtonEnabled: Boolean = false,
  val isResetButtonEnabled: Boolean = false,
)

internal fun FilterViewState.toFilterParams() = FilterParams(
  classificationNames = classificationIds,
  dateType = dateType,
  sortType = sortType,
  selectedDates = selectedDates.takeIf { dateType == DateType.Custom },
)

fun FilterParams.isFilterUnChanged() = classificationNames.isNullOrEmpty()
    && selectedDates.isNullOrEmpty()
    && dateType in listOf(DateType.AnyDate, DateType.Custom)
    && sortType?.sortingOption == SortingOption.Relevance
    && sortType?.isAscending == true

internal fun FilterParams.toFilterViewState(
  isApplyButtonEnabled: Boolean = false,
  isResetButtonEnabled: Boolean = false,
) = FilterViewState(
  classificationIds = classificationNames.orEmpty(),
  dateType = dateType,
  sortType = sortType ?: SortType(
    sortingOption = SortingOption.Relevance,
    isAscending = true,
  ),
  isApplyButtonEnabled = isApplyButtonEnabled,
  isResetButtonEnabled = isResetButtonEnabled,
  selectedDates = selectedDates,
)