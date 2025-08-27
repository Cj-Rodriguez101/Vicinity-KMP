package com.cjproductions.vicinity.discover.presentation.countrySelector

sealed interface CountrySelectorState {
  data object Loading: CountrySelectorState
  data class Loaded(
    val selectedCountryCode: String?,
    val countries: List<CountryUI>,
  ): CountrySelectorState
}