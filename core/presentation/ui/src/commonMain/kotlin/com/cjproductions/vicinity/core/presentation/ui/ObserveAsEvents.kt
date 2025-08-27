package com.cjproductions.vicinity.core.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.withContext

@Composable
fun <T> Flow<T>.observeAsEvents(
  vararg keys: Any?,
  onEvent: suspend (T) -> Unit,
) {
  LaunchedEffect(this, *keys) {
    withContext(Dispatchers.Main.immediate) {
      this@observeAsEvents.distinctUntilChanged()
        .collectLatest(onEvent)
    }
  }
}



