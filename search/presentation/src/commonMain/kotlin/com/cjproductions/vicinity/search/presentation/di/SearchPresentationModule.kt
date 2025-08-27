package com.cjproductions.vicinity.search.presentation.di

import com.cjproductions.vicinity.search.domain.SearchAndUpdateUseCase
import com.cjproductions.vicinity.search.presentation.datePicker.DatePickerViewModel
import com.cjproductions.vicinity.search.presentation.filter.FilterViewModel
import com.cjproductions.vicinity.search.presentation.search.SearchViewModel
import com.cjproductions.vicinity.search.presentation.searchResults.SearchResultViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val searchPresentationModule = module {
  viewModelOf(::SearchViewModel)
  viewModelOf(::SearchResultViewModel)
  viewModelOf(::FilterViewModel)
  viewModelOf(::DatePickerViewModel)
  singleOf(::SearchAndUpdateUseCase)
}