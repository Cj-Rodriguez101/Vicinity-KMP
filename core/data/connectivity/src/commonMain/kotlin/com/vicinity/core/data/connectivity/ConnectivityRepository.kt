package com.vicinity.core.data.connectivity

import com.cjproductions.vicinity.support.tools.AppDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

interface ConnectivityRepository {
  val statusUpdates: SharedFlow<Boolean>
}

class DefaultConnectivityRepository(
  private val platformConnectivityManager: PlatformConnectivityManager,
  appDispatcher: AppDispatcher,
): ConnectivityRepository, CoroutineScope {

  override val coroutineContext: CoroutineContext = SupervisorJob() + appDispatcher.io
  private val _statusUpdates = MutableSharedFlow<Boolean>()

  init {
    launch {
      platformConnectivityManager.connectivity.statusUpdates.collect { status ->
        _statusUpdates.emit(status.isConnected)
      }
    }
  }

  override val statusUpdates = _statusUpdates.distinctUntilChanged()
    .shareIn(this, SharingStarted.Lazily)
}