package com.cjproductions.vicinity.search.presentation.filter

sealed interface FilterDestination {
  data class DatePicker(val dates: List<Double>): FilterDestination
  data object Back: FilterDestination
}