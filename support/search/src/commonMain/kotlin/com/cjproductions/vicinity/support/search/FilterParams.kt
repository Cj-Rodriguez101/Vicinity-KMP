package com.cjproductions.vicinity.support.search

import com.cjproductions.vicinity.core.domain.ClassificationName
import kotlinx.datetime.LocalDateTime

data class FilterParams(
  val dateType: DateType,
  val selectedDates: List<LocalDateTime>? = null,
  val classificationNames: List<ClassificationName>?,
  val sortType: SortType?,
)

fun FilterParams?.toFilterCount(): Int {
  if (this == null) return 0
  var count = 0
  if (dateType != DateType.AnyDate) count++
  if (classificationNames?.isNotEmpty() == true) count++
  if (sortType != null) count++
  return count
}

data class SortType(
  val sortingOption: SortingOption,
  val isAscending: Boolean = true,
)

enum class SortingOption(val value: String) {
  Relevance("relevance"),
  Date("date"),
  Name("name"),
}

enum class DateType {
  AnyDate,
  Today,
  Tomorrow,
  ThisWeekend,
  Custom,
}