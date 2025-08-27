package com.cjproductions.vicinity.support.haptics.presentation

import com.cjproductions.vicinity.support.haptics.domain.HapticService

expect class PlatformHapticsService: HapticService {
  override fun performClickHaptics()
}