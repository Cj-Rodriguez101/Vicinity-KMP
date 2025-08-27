package com.cjproductions.vicinity.core.data.preference.di

import org.koin.core.module.Module
import org.koin.dsl.module

expect val platformPreferenceModule: Module

val corePreferenceModule = module {
  includes(platformPreferenceModule)
}