@file:OptIn(ExperimentalCoroutinesApi::class)

package com.cjproductions.vicinity.search.presentation.datePicker

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cjproductions.vicinity.support.tools.toLocalDateTime
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.LocalDateTime

class DatePickerViewModel(
  savedStateHandle: SavedStateHandle,
): ViewModel() {

  val dates: StateFlow<List<LocalDateTime>> = savedStateHandle.getStateFlow<DoubleArray?>(
    key = DATE_PICKER_KEY,
    initialValue = null,
  ).mapLatest { it?.toList()?.mapNotNull { it.toLocalDateTime() }.orEmpty() }
    .stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(5000),
      emptyList()
    )
}

const val DATE_PICKER_KEY = "dates"