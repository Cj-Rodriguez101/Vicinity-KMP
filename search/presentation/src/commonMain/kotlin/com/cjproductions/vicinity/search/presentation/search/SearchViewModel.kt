package com.cjproductions.vicinity.search.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cjproductions.vicinity.core.domain.util.map
import com.cjproductions.vicinity.core.presentation.ui.components.DefaultValidationState
import com.cjproductions.vicinity.location.domain.LocationRepository
import com.cjproductions.vicinity.location.domain.model.getCountry
import com.cjproductions.vicinity.search.domain.SearchAndUpdateUseCase
import com.cjproductions.vicinity.search.domain.SearchRepository
import com.cjproductions.vicinity.search.domain.model.RecentSearch
import com.cjproductions.vicinity.search.presentation.search.SearchAction.OnBackClick
import com.cjproductions.vicinity.search.presentation.search.SearchAction.OnClearEventSearch
import com.cjproductions.vicinity.search.presentation.search.SearchAction.OnClearLocationSearch
import com.cjproductions.vicinity.search.presentation.search.SearchAction.OnDeleteSearchItem
import com.cjproductions.vicinity.search.presentation.search.SearchAction.OnEventSearchChanged
import com.cjproductions.vicinity.search.presentation.search.SearchAction.OnLocationSearchChanged
import com.cjproductions.vicinity.search.presentation.search.SearchAction.OnSearchClicked
import com.cjproductions.vicinity.search.presentation.search.SearchAction.OnSearchItemClicked
import com.cjproductions.vicinity.search.presentation.search.SearchDestination.Back
import com.cjproductions.vicinity.search.presentation.search.SearchDestination.SearchResult
import com.cjproductions.vicinity.search.presentation.search.SearchViewModel.Companion.SearchType.EVENT
import com.cjproductions.vicinity.search.presentation.search.SearchViewModel.Companion.SearchType.LOCATION
import com.cjproductions.vicinity.support.search.SearchFilterParamsRepository
import com.cjproductions.vicinity.support.tools.Debouncer
import com.cjproductions.vicinity.support.tools.normalizeText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
  private val searchRepository: SearchRepository,
  private val searchFilterParamsRepository: SearchFilterParamsRepository,
  private val searchAndUpdate: SearchAndUpdateUseCase,
  private val locationRepository: LocationRepository,
  private val deBouncer: Debouncer,
): ViewModel() {

  private val _uiState = MutableStateFlow(SearchViewState())
  val uiState = _uiState.asStateFlow()

  private val _destination = Channel<SearchDestination>()
  val destination = _destination.receiveAsFlow()

  init {
    viewModelScope.launch {
      _uiState.update {
        it.copy(
          eventSearch = FormField(
            field = searchFilterParamsRepository.searchParams.value?.keyword.orEmpty(),
            state = DefaultValidationState(
              isEmpty = searchFilterParamsRepository.searchParams.value?.keyword.isNullOrEmpty()
            )
          ),
          locationSearch = FormField(
            field = searchFilterParamsRepository.searchParams.value?.location
              ?: locationRepository.location.value?.getCountry().orEmpty(),
          )
        )
      }
      searchRepository.searchItems.collectLatest { recentSearches ->
        _uiState.update { it.copy(recentSearches = recentSearches) }
      }
    }
  }

  fun onAction(action: SearchAction) {
    when (action) {
      is OnEventSearchChanged -> updateEvent(action.eventSearch)

      is OnLocationSearchChanged -> updateLocation(action.locationSearch)

      OnSearchClicked -> searchEvents(
        recentSearch = RecentSearch(
          normalizedName = uiState.value.eventSearch.field.normalizeText(),
          name = uiState.value.eventSearch.field.takeIf { it.isNotEmpty() },
          location = uiState.value.locationSearch.field.takeIf { it.isNotEmpty() },
        )
      )

      is OnSearchItemClicked -> searchEvents(action.searchItem)

      OnClearEventSearch -> clearFields()

      OnClearLocationSearch -> clearFields()

      is OnDeleteSearchItem -> deleteSearchItem(action.searchTerm)

      OnBackClick -> resetAndGoBack()
    }
  }

  private fun updateEvent(eventSearch: String) {
    viewModelScope.launch {
      _uiState.update {
        it.copy(
          eventSearch = FormField(
            field = eventSearch,
            state = DefaultValidationState(
              isEmpty = eventSearch.isEmpty()
            )
          ),
          isSearchButtonEnabled = eventSearch.isNotEmpty()
              || uiState.value.locationSearch.field.isNotEmpty()
        )
      }

      if (eventSearch.length < MIN_SEARCH_LENGTH || eventSearch.isEmpty()) return@launch
      debouncedSearch(
        query = eventSearch.trim(),
        searchType = EVENT,
      )
    }
  }

  private fun updateLocation(location: String) {
    viewModelScope.launch {
      _uiState.update {
        it.copy(
          locationSearch = FormField(location),
          isSearchButtonEnabled = location.isNotEmpty()
              || uiState.value.eventSearch.field.isNotEmpty(),
        )
      }

      if (location.length < MIN_SEARCH_LENGTH || location.isEmpty()) return@launch
      debouncedSearch(
        query = location.trim(),
        searchType = LOCATION,
      )
    }
  }

  private fun clearFields() {
    _uiState.update {
      it.copy(
        locationSearch = FormField(
          field = "",
          state = DefaultValidationState(isEmpty = true),
        ),
        currentSearches = emptyList(),
        isSearchButtonEnabled = uiState.value.eventSearch.field.isNotEmpty()
            && uiState.value.locationSearch.field.isNotEmpty()
      )
    }
  }

  private fun deleteSearchItem(searchTerm: RecentSearch) {
    viewModelScope.launch {
      searchRepository.deleteQuery(recentSearch = searchTerm)
    }
  }

  private fun resetAndGoBack() {
    viewModelScope.launch {
      searchFilterParamsRepository.updateSearchParams(null)
      _destination.send(Back)
    }
  }

  private fun searchEvents(recentSearch: RecentSearch) {
    viewModelScope.launch {
      _uiState.update { it.copy(isSearchButtonLoading = true) }
      searchAndUpdate(recentSearch)

      _uiState.update {
        it.copy(
          eventSearch = FormField(
            field = recentSearch.name.orEmpty(),
            state = DefaultValidationState(isEmpty = recentSearch.name.isNullOrEmpty())
          ),
          locationSearch = FormField(
            field = recentSearch.location.orEmpty(),
          ),
          isSearchButtonLoading = false,
        )
      }
      _destination.send(SearchResult)
    }
  }

  private fun debouncedSearch(
    query: String,
    searchType: SearchType,
  ) {
    deBouncer.debounceAction(scope = viewModelScope) {
      when (searchType) {
        EVENT -> {
          searchRepository.fetchEventsBasedOnKeyword(query).map { locations ->
            _uiState.update {
              it.copy(
                currentSearches = locations.events.map {
                  CurrentSearch(
                    keyword = it.name,
                  )
                }.distinctBy { it.keyword }
              )
            }
          }
        }

        LOCATION -> {
          searchRepository.fetchLocations(query).map { locations ->
            _uiState.update {
              it.copy(
                currentSearches = locations.map { geoCodeLocation ->
                  with(geoCodeLocation) {
                    CurrentSearch(
                      location = displayPlace,
                      latitude = latitude,
                      longitude = longitude,
                    )
                  }
                }.distinctBy { it.location }
              )
            }
          }
        }
      }
    }
  }

  companion object {
    private const val MIN_SEARCH_LENGTH = 3

    private enum class SearchType {
      EVENT,
      LOCATION,
    }
  }
}