package com.cjproductions.vicinity.search.presentation.filter

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cjproductions.vicinity.core.domain.ClassificationName
import com.cjproductions.vicinity.search.presentation.datePicker.DATE_PICKER_KEY
import com.cjproductions.vicinity.search.presentation.filter.FilterDestination.Back
import com.cjproductions.vicinity.search.presentation.filter.FilterDestination.DatePicker
import com.cjproductions.vicinity.search.presentation.filter.FilterViewAction.OnApplyFilters
import com.cjproductions.vicinity.search.presentation.filter.FilterViewAction.OnGoBackClicked
import com.cjproductions.vicinity.search.presentation.filter.FilterViewAction.OnResetFilters
import com.cjproductions.vicinity.search.presentation.filter.FilterViewAction.OnToggleSortOrder
import com.cjproductions.vicinity.search.presentation.filter.FilterViewAction.OnUpdateClassification
import com.cjproductions.vicinity.search.presentation.filter.FilterViewAction.OnUpdateDateType
import com.cjproductions.vicinity.search.presentation.filter.FilterViewAction.OnUpdateSortOption
import com.cjproductions.vicinity.support.search.DateType
import com.cjproductions.vicinity.support.search.FilterParams
import com.cjproductions.vicinity.support.search.SearchFilterParamsRepository
import com.cjproductions.vicinity.support.search.SortType
import com.cjproductions.vicinity.support.tools.toDouble
import com.cjproductions.vicinity.support.tools.toLocalDateTime
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FilterViewModel(
  private val searchFilterParamsRepository: SearchFilterParamsRepository,
  val savedStateHandle: SavedStateHandle,
): ViewModel() {

  private val _destination = Channel<FilterDestination>()
  val destination = _destination.receiveAsFlow()

  private val _uiState = MutableStateFlow(
    searchFilterParamsRepository.filterParams.value?.toFilterViewState() ?: FilterViewState()
  )
  val uiState = _uiState.asStateFlow()

  @OptIn(ExperimentalCoroutinesApi::class)
  private val dates = savedStateHandle.getStateFlow<Array<Double>?>(
    key = DATE_PICKER_KEY,
    initialValue = null,
  ).mapLatest { it?.mapNotNull { date -> date.toLocalDateTime() } }

  init {
    viewModelScope.launch {
      dates.collectLatest { dates ->
        if (dates.isNullOrEmpty()) return@collectLatest
        _uiState.update { state ->
          state.copy(
            selectedDates = dates,
            isApplyButtonEnabled = true,
          )
        }
      }
    }
  }

  fun onAction(action: FilterViewAction) {
    when (action) {
      is OnUpdateDateType -> updateDateType(action)

      is OnUpdateSortOption -> updateSortOption(action)

      OnToggleSortOrder -> updateToggleSortOrder()

      is OnUpdateClassification -> updateClassification(action.classificationName)

      OnApplyFilters -> updateFilters()

      OnGoBackClicked -> goBack()

      OnResetFilters -> updateFilters(null)
    }
  }

  private fun updateSortOption(action: OnUpdateSortOption) {
    viewModelScope.launch {
      _uiState.update {
        it.copy(
          isApplyButtonEnabled = true,
          sortType = SortType(
            sortingOption = action.sortingOption,
            isAscending = it.sortType.isAscending,
          )
        )
      }
    }
  }

  private fun updateDateType(action: OnUpdateDateType) {
    viewModelScope.launch {
      _uiState.update {
        it.copy(
          dateType = action.dateType,
          isApplyButtonEnabled = (action.dateType
              == DateType.Custom && uiState.value.selectedDates?.isNotEmpty() == true)
              || action.dateType != DateType.Custom,
          isResetButtonEnabled = true,
        )
      }
      if (action.dateType == DateType.Custom) {
        _destination.send(DatePicker(action.dates?.map { it.toDouble() }.orEmpty()))
      }
    }
  }

  private fun updateToggleSortOrder() {
    viewModelScope.launch {
      _uiState.update {
        it.copy(
          sortType = it.sortType.copy(isAscending = it.sortType.isAscending.not()),
          isApplyButtonEnabled = true,
        )
      }
    }
  }

  private fun updateClassification(classificationName: ClassificationName) {
    val currentClassifications = uiState.value.classificationIds
    val updatedClassifications =
      if (currentClassifications.contains(classificationName)) {
        currentClassifications.toMutableList().minus(classificationName)
      } else {
        currentClassifications.toMutableList().plus(classificationName)
      }
    _uiState.update {
      it.copy(
        classificationIds = updatedClassifications,
        isApplyButtonEnabled = true,
        isResetButtonEnabled = true,
      )
    }
  }

  private fun updateFilters() {
    viewModelScope.launch {
      updateFilters(
        uiState.value.toFilterParams().takeUnless { it.isFilterUnChanged() })
      _destination.send(Back)
    }
  }

  private fun goBack() {
    viewModelScope.launch { _destination.send(Back) }
  }

  private fun updateFilters(filterParams: FilterParams?) {
    viewModelScope.launch {
      searchFilterParamsRepository.updateFilterParams(filterParams)
      _uiState.update { filterParams?.toFilterViewState() ?: FilterViewState() }
    }
  }
}