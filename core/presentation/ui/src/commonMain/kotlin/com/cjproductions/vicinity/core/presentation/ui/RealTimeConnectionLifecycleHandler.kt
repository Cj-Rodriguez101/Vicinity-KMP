package com.cjproductions.vicinity.core.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle.Event.ON_PAUSE
import androidx.lifecycle.Lifecycle.Event.ON_RESUME
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun RealTimeConnectionLifecycleHandler(
  networkStatusUpdates: SharedFlow<Boolean>,
  onPauseRealTimeConnection: () -> Unit,
  onResumeRealTimeConnection: () -> Unit,
) {
  val lifecycleOwner = LocalLifecycleOwner.current

  DisposableEffect(lifecycleOwner) {
    val observer = LifecycleEventObserver { _, event ->
      when (event) {
        ON_PAUSE -> onPauseRealTimeConnection()

        ON_RESUME -> onResumeRealTimeConnection()

        else -> Unit
      }
    }
    lifecycleOwner.lifecycle.addObserver(observer)
    onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
  }

  LaunchedEffect(Unit) {
    networkStatusUpdates.collectLatest { isConnected ->
      if (isConnected) {
        onResumeRealTimeConnection()
      } else {
        onPauseRealTimeConnection()
      }
    }
  }
}