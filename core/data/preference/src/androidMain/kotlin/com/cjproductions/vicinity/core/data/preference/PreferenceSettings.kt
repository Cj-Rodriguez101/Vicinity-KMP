package com.cjproductions.vicinity.core.data.preference

import android.app.Application
import androidx.preference.PreferenceManager
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.SharedPreferencesSettings
import com.russhwolf.settings.observable.makeObservable

actual class PreferenceSettings(app: Application) {
  actual val settings: ObservableSettings =
    SharedPreferencesSettings(PreferenceManager.getDefaultSharedPreferences(app)).makeObservable()
}