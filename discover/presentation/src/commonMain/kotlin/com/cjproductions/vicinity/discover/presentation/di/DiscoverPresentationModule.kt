package com.cjproductions.vicinity.discover.presentation.di

import com.cjproductions.vicinity.discover.presentation.countrySelector.CountrySelectorViewModel
import com.cjproductions.vicinity.discover.presentation.discover.DiscoverViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val discoverPresentationModule = module {
  viewModelOf(::DiscoverViewModel)
  viewModelOf(::CountrySelectorViewModel)
}