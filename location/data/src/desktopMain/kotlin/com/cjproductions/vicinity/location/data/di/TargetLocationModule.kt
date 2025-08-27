package com.cjproductions.vicinity.location.data.di

import com.cjproductions.vicinity.location.data.PlatformLocator
import com.cjproductions.vicinity.location.domain.DomainLocationTracker
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val targetLocationModule = module {
  singleOf(::PlatformLocator).bind<DomainLocationTracker>()
}