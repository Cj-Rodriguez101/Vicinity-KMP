package com.cjproductions.vicinity.support.haptics.presentation.di

import com.cjproductions.vicinity.support.haptics.domain.HapticService
import com.cjproductions.vicinity.support.haptics.presentation.PlatformHapticsService
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformHapticsModule: Module = module {
  singleOf(::PlatformHapticsService).bind<HapticService>()
}