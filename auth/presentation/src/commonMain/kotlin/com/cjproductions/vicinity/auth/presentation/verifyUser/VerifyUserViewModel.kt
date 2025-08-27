package com.cjproductions.vicinity.auth.presentation.verifyUser

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cjproductions.vicinity.auth.domain.AuthRepository
import com.cjproductions.vicinity.auth.domain.validator.UserValidationState.VerificationCodeValidationState
import com.cjproductions.vicinity.auth.presentation.components.FormField
import com.cjproductions.vicinity.auth.presentation.verifyUser.VerifyDestination.Back
import com.cjproductions.vicinity.auth.presentation.verifyUser.VerifyDestination.Discover
import com.cjproductions.vicinity.auth.presentation.verifyUser.VerifyUserAction.OnBackClick
import com.cjproductions.vicinity.auth.presentation.verifyUser.VerifyUserAction.OnCodeChange
import com.cjproductions.vicinity.auth.presentation.verifyUser.VerifyUserAction.OnResendCode
import com.cjproductions.vicinity.auth.presentation.verifyUser.VerifyUserAction.OnSubmit
import com.cjproductions.vicinity.core.domain.util.onFailure
import com.cjproductions.vicinity.core.domain.util.onSuccess
import com.cjproductions.vicinity.core.presentation.designsystem.components.UIText.StringResourceId
import com.cjproductions.vicinity.core.presentation.ui.SnackBarController
import com.cjproductions.vicinity.core.presentation.ui.SnackBarEvent.SnackBarUiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import vicinity.auth.presentation.generated.resources.Res
import vicinity.auth.presentation.generated.resources.failed_to_verify_otp
import vicinity.auth.presentation.generated.resources.resend_failed
import vicinity.auth.presentation.generated.resources.resend_success

class VerifyUserViewModel(
  private val authRepository: AuthRepository,
  private val timerRepository: TimerRepository,
  private val snackBarController: SnackBarController,
  savedStateHandle: SavedStateHandle,
): ViewModel() {

  val source = savedStateHandle.get<String>(SOURCE_KEY)
  private val email = savedStateHandle.get<String>(EMAIL_KEY)

  private val _verifyDestination = Channel<VerifyDestination>()
  val verifyDestination = _verifyDestination.receiveAsFlow()

  private val _uiState = MutableStateFlow(VerifyUserState())
  val uiState = combine(_uiState, timerRepository.timer) { state, timer ->
    state.copy(
      timeElapsed = (timer as? TimerState.Running)?.timeElapsed,
      isEnabled = state.otpField.state?.isValid == true && timer is TimerState.Running,
    )
  }.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = VerifyUserState()
  )

  init {
    if (source != null && email != null) {
      viewModelScope.launch {
        authRepository.resendForgotPasswordVerificationCode(email)
      }
    }
  }

  fun onAction(action: VerifyUserAction) {
    when (action) {
      is OnCodeChange -> updateVerificationCode(action.code)

      OnBackClick -> goBack()

      OnResendCode -> resendVerificationCode()

      is OnSubmit -> verifyUser()
    }
  }

  private fun verifyUser() {
    viewModelScope.launch {
      _uiState.update { it.copy(isLoading = true) }
      val result = if (source != null) {
        authRepository.verifyRecoveryTokenHash(uiState.value.otpField.field)
      } else {
        authRepository.verifySignUpTokenHash(uiState.value.otpField.field)
      }
      result.onSuccess {
        _verifyDestination.send(Discover)
      }.onFailure {
        snackBarController.sendEvent(SnackBarUiEvent(StringResourceId(Res.string.failed_to_verify_otp)))
      }
      _uiState.update { it.copy(isLoading = false) }
    }
  }

  private fun resendVerificationCode() {
    viewModelScope.launch {
      if (email == null) return@launch
      timerRepository.startTimer()
      val result = if (source != null) {
        authRepository.resendForgotPasswordVerificationCode(email)
      } else {
        authRepository.resendSignUpVerificationCode(email)
      }
      snackBarController.sendEvent(
        SnackBarUiEvent(StringResourceId(Res.string.resend_success.takeIf { result.isSuccess }
          ?: Res.string.resend_failed))
      )
    }
  }

  private fun goBack() {
    viewModelScope.launch { _verifyDestination.send(Back) }
  }

  private fun updateVerificationCode(code: String) {
    val validationState = VerificationCodeValidationState(code.isEmpty())
    _uiState.update { state ->
      state.copy(
        otpField = FormField(
          field = code,
          state = validationState,
        ),
        isEnabled = validationState.isValid,
      )
    }
  }

  companion object {
    private const val EMAIL_KEY = "email"
    private const val SOURCE_KEY = "source"
  }
}