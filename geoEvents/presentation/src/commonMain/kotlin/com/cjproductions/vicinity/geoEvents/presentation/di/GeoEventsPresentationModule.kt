package com.cjproductions.vicinity.geoEvents.presentation.di

import com.cjproductions.vicinity.geoEvents.presentation.globalEvents.GlobalEventViewModel
import com.cjproductions.vicinity.geoEvents.presentation.locationSelector.LocationSelectorViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val geoEventsPresentationModule = module {
  viewModelOf(::LocationSelectorViewModel)
  viewModelOf(::GlobalEventViewModel)
}