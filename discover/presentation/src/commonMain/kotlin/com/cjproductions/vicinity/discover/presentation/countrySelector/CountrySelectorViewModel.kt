package com.cjproductions.vicinity.discover.presentation.countrySelector

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cjproductions.vicinity.discover.presentation.countrySelector.CountrySelectorState.Loaded
import com.cjproductions.vicinity.discover.presentation.countrySelector.CountrySelectorState.Loading
import com.cjproductions.vicinity.location.domain.LocationRepository
import com.cjproductions.vicinity.location.domain.model.getCountryCode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CountrySelectorViewModel(
  private val locationRepository: LocationRepository,
): ViewModel() {

  private val _selectedCountryCode = MutableStateFlow<String?>(null)
  val selectedCountryCode = _selectedCountryCode.asStateFlow()

  private val _uiState = MutableStateFlow(Loading)
  val uiState: StateFlow<CountrySelectorState> =
    combine(_uiState, selectedCountryCode) { uiState, selectedCountryCode ->
      Loaded(
        selectedCountryCode = selectedCountryCode,
        countries = locationRepository.getAllCountries().map { it.toCountryUI() },
      )
    }.stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(5000),
      Loading,
    )

  init {
    viewModelScope.launch {
      _selectedCountryCode.update { locationRepository.location.value?.getCountryCode() ?: US_CODE }
    }
  }


  fun onSetCountry(country: CountryUI) {
    viewModelScope.launch {
      locationRepository.updateUserSetCountry(country.toCountryInfo())
      _selectedCountryCode.update { country.countryCode }
    }
  }

  companion object {
    private const val US_CODE = "us"
  }
}