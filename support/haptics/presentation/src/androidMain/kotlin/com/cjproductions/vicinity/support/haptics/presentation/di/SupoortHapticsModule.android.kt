package com.cjproductions.vicinity.support.haptics.presentation.di

import android.content.Context
import android.os.Build
import android.os.Vibrator
import android.os.VibratorManager
import com.cjproductions.vicinity.support.haptics.domain.HapticService
import com.cjproductions.vicinity.support.haptics.presentation.PlatformHapticsService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformHapticsModule = module {
  single<Vibrator> {
    val context: Context = get() // Get the app context from Koin
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
      val vibratorManager =
        context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
      vibratorManager.defaultVibrator
    } else {
      @Suppress("DEPRECATION")
      context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }
  }
  singleOf(::PlatformHapticsService).bind<HapticService>()
}