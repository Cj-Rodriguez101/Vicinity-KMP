package com.cjproductions.vicinity.core.data.preference.di

import com.cjproductions.vicinity.core.data.preference.PreferenceSettings
import com.russhwolf.settings.ObservableSettings
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

actual val platformPreferenceModule = module {
  single { PreferenceSettings(androidApplication()) }
  single<ObservableSettings> { get<PreferenceSettings>().settings }
}