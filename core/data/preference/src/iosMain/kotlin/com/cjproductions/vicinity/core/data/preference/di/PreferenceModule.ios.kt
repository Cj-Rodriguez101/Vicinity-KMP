package com.cjproductions.vicinity.core.data.preference.di

import com.cjproductions.vicinity.core.data.preference.PreferenceSettings
import com.russhwolf.settings.ObservableSettings
import org.koin.dsl.module

actual val platformPreferenceModule = module {
  single { PreferenceSettings() }
  single<ObservableSettings> { get<PreferenceSettings>().settings }
}