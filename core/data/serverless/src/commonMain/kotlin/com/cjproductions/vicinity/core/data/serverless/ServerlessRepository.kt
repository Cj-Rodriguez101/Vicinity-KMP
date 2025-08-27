package com.cjproductions.vicinity.core.data.serverless

import com.cjproductions.vicinity.core.data.serverless.remote.RemoteServerlessDataSource
import com.cjproductions.vicinity.core.domain.serverless.LifecycleState
import com.cjproductions.vicinity.core.domain.serverless.ServerlessRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DefaultServerlessRepository(
  private val remoteServerlessDataSource: RemoteServerlessDataSource,
): ServerlessRepository {

  private val _lifecycleState: MutableStateFlow<LifecycleState> = MutableStateFlow(
    LifecycleState.RESUMED
  )
  override val lifecycleState = _lifecycleState.asStateFlow()

  override suspend fun pauseRealtimeConnection() {
    runCatching {
      remoteServerlessDataSource.unsubscribeAllChannels()
      _lifecycleState.update { LifecycleState.PAUSED }
    }
  }

  override suspend fun resumeRealtimeConnection() {
    _lifecycleState.update { LifecycleState.RESUMED }
  }
}