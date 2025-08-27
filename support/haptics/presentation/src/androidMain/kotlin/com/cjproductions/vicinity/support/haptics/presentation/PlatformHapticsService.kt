package com.cjproductions.vicinity.support.haptics.presentation

import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import com.cjproductions.vicinity.support.haptics.domain.HapticService

actual class PlatformHapticsService(val vibrator: Vibrator): HapticService {
    actual override fun performClickHaptics() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(500)
        }
    }
}