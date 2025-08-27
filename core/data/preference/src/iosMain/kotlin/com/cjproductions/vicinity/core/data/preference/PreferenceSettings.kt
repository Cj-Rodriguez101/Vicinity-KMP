@file:OptIn(ExperimentalSettingsApi::class)

package com.cjproductions.vicinity.core.data.preference

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.observable.makeObservable
import platform.Foundation.NSUserDefaults

@Suppress("DEPRECATION")
actual class PreferenceSettings() {
  actual val settings: ObservableSettings =
    NSUserDefaultsSettings(NSUserDefaults.standardUserDefaults).makeObservable()
}