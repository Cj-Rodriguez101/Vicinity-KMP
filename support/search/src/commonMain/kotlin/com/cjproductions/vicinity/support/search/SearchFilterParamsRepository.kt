package com.cjproductions.vicinity.support.search

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

interface SearchFilterParamsRepository {
  val searchParams: StateFlow<SearchParams?>
  val filterParams: StateFlow<FilterParams?>
  suspend fun updateSearchParams(searchParams: SearchParams?)
  suspend fun updateFilterParams(filterParams: FilterParams?)
}

class DefaultSearchFilterParamsRepository(): SearchFilterParamsRepository {
  private val _searchParams = MutableStateFlow<SearchParams?>(null)
  private val _filterParams = MutableStateFlow<FilterParams?>(null)

  override val searchParams = _searchParams.asStateFlow()
  override val filterParams = _filterParams.asStateFlow()

  override suspend fun updateSearchParams(searchParams: SearchParams?) {
    _searchParams.update { searchParams }
  }

  override suspend fun updateFilterParams(filterParams: FilterParams?) {
    _filterParams.update { filterParams }
  }
}