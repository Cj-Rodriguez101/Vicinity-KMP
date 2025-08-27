package com.cjproductions.vicinity.auth.presentation.logOut

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cjproductions.vicinity.auth.domain.LogOutUseCase
import com.cjproductions.vicinity.core.domain.util.onFailure
import com.cjproductions.vicinity.core.domain.util.onSuccess
import com.cjproductions.vicinity.core.presentation.designsystem.components.UIText
import com.cjproductions.vicinity.core.presentation.ui.SnackBarController
import com.cjproductions.vicinity.core.presentation.ui.SnackBarEvent.SnackBarUiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import vicinity.auth.presentation.generated.resources.Res
import vicinity.auth.presentation.generated.resources.failed_to_logout

class LogOutViewModel(
  private val logOutUseCase: LogOutUseCase,
  private val snackBarController: SnackBarController,
): ViewModel() {

  private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
  val isLoading = _isLoading.asStateFlow()

  private val _destination = Channel<LogoutDestination>()
  val destination = _destination.receiveAsFlow()

  fun logOut() {
    viewModelScope.launch {
      _isLoading.update { true }
      logOutUseCase().onSuccess {
        _destination.send(LogoutDestination.Back)
      }.onFailure {
        snackBarController.sendEvent(
          SnackBarUiEvent(message = UIText.StringResourceId(Res.string.failed_to_logout))
        )
      }
      _isLoading.update { false }
    }

  }
}