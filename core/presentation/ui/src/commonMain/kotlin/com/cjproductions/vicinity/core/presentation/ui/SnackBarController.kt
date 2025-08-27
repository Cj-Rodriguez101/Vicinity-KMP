package com.cjproductions.vicinity.core.presentation.ui

import androidx.compose.material3.SnackbarDuration
import com.cjproductions.vicinity.core.presentation.designsystem.components.UIText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

sealed class SnackBarEvent {
  data class SnackBarUiEvent(
    val message: UIText,
    val duration: SnackbarDuration = SnackbarDuration.Short,
    val action: SnackBarAction? = null,
  ): SnackBarEvent()

  data object DismissSnackBarEvent: SnackBarEvent()
}

data class SnackBarAction(
  val name: String,
  val action: suspend () -> Unit,
)

class SnackBarController {
  private val _events = Channel<SnackBarEvent>()
  val events = _events.receiveAsFlow()

  suspend fun sendEvent(event: SnackBarEvent) {
    _events.send(event)
  }
}