package com.cjproductions.vicinity.profile.data.di

import com.cjproductions.vicinity.profile.data.PlatformStorage
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val profileStorageModule = module {
  singleOf(::PlatformStorage)
}