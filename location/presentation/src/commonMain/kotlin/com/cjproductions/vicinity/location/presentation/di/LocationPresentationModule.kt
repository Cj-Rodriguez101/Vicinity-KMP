package com.cjproductions.vicinity.location.presentation.di

import org.koin.core.module.Module
import org.koin.dsl.module

expect val targetTileProviderModule: Module

val locationPresentationModule = module {
  includes(targetTileProviderModule)
}