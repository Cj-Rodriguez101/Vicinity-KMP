package com.cjproductions.vicinity.support.haptics.presentation.di

import com.cjproductions.vicinity.support.haptics.presentation.PerformClickHapticsUseCase
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

expect val platformHapticsModule: Module

val supportHapticsModule = module {
  includes(platformHapticsModule)
  singleOf(::PerformClickHapticsUseCase)
}