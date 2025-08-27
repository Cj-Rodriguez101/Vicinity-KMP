package com.cjproductions.vicinity.support.haptics.presentation

import com.cjproductions.vicinity.support.haptics.domain.HapticService

class PerformClickHapticsUseCase(
  private val hapticsService: HapticService,
) {
  operator fun invoke() {
    hapticsService.performClickHaptics()
  }
}