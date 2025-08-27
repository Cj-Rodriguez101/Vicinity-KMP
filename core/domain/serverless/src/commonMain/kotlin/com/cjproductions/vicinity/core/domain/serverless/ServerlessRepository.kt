package com.cjproductions.vicinity.core.domain.serverless

import com.cjproductions.vicinity.core.domain.util.Error
import kotlinx.coroutines.flow.StateFlow

interface ServerlessRepository {
  val lifecycleState: StateFlow<LifecycleState>
  suspend fun pauseRealtimeConnection()
  suspend fun resumeRealtimeConnection()
}

sealed class ServerlessError: Error {
  data class Unknown(val message: String): ServerlessError()
}

enum class LifecycleState {
  RESUMED,
  PAUSED,
}