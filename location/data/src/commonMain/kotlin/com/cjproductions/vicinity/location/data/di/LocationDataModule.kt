package com.cjproductions.vicinity.location.data.di

import com.cjproductions.vicinity.location.data.DefaultLocationDataSource
import com.cjproductions.vicinity.location.data.DefaultLocationRepository
import com.cjproductions.vicinity.location.data.LocationDataSource
import com.cjproductions.vicinity.location.domain.LocationRepository
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val locationDataModule = module {
  singleOf(::DefaultLocationDataSource).bind<LocationDataSource>()
  singleOf(::DefaultLocationRepository).bind<LocationRepository>()
  includes(targetLocationModule)
}

expect val targetLocationModule: Module