package com.cjproductions.vicinity.support.haptics.presentation

import com.cjproductions.vicinity.support.haptics.domain.HapticService
import platform.UIKit.UIImpactFeedbackGenerator
import platform.UIKit.UIImpactFeedbackStyle.UIImpactFeedbackStyleLight

actual class PlatformHapticsService(): HapticService {
  actual override fun performClickHaptics() {
    val impactFeedback = UIImpactFeedbackGenerator(UIImpactFeedbackStyleLight)
    impactFeedback.impactOccurred()
  }
}

