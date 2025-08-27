package com.cjproductions.vicinity.auth.presentation.forgotPassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cjproductions.vicinity.auth.domain.AuthRepository
import com.cjproductions.vicinity.auth.domain.validator.IsEmailValidUseCase
import com.cjproductions.vicinity.auth.presentation.forgotPassword.ForgotPasswordAction.OnBackClick
import com.cjproductions.vicinity.auth.presentation.forgotPassword.ForgotPasswordAction.OnEmailChange
import com.cjproductions.vicinity.auth.presentation.forgotPassword.ForgotPasswordAction.OnSubmitClick
import com.cjproductions.vicinity.auth.presentation.forgotPassword.ForgotPasswordDestination.Back
import com.cjproductions.vicinity.auth.presentation.forgotPassword.ForgotPasswordDestination.Verification
import com.cjproductions.vicinity.core.presentation.designsystem.components.UIText.StringResourceId
import com.cjproductions.vicinity.core.presentation.ui.SnackBarController
import com.cjproductions.vicinity.core.presentation.ui.SnackBarEvent.SnackBarUiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import vicinity.auth.presentation.generated.resources.Res
import vicinity.auth.presentation.generated.resources.failed_to_reset_password

class ForgotPasswordViewModel(
    private val isEmailValidUseCase: IsEmailValidUseCase,
    private val authRepository: AuthRepository,
    private val snackBarController: SnackBarController,
): ViewModel() {
    private val _forgotPasswordDestination = Channel<ForgotPasswordDestination>()
    val forgotPasswordDestination = _forgotPasswordDestination.receiveAsFlow()

    private val _uiState = MutableStateFlow(ForgotPasswordViewState())
    val uiState = _uiState.asStateFlow()

    fun onAction(action: ForgotPasswordAction) {
        when (action) {
            is OnEmailChange -> updateEmail(action.email)

            OnSubmitClick -> resetPassword()

            OnBackClick -> goBack()
        }
    }

    private fun resetPassword() {
        viewModelScope.launch {
            _uiState.update { state -> state.copy(loading = true) }
            val result = authRepository.resetPassword(_uiState.value.email.field.trim())
            if (result.isSuccess) {
                _forgotPasswordDestination.send(Verification(_uiState.value.email.field.trim()))
            } else {
                snackBarController.sendEvent(
                    SnackBarUiEvent(StringResourceId(Res.string.failed_to_reset_password))
                )
            }
            _uiState.update { state -> state.copy(loading = false) }
        }
    }

    private fun goBack() {
        viewModelScope.launch { _forgotPasswordDestination.send(Back) }
    }

    private fun updateEmail(email: String) {
        val emailValidationState = isEmailValidUseCase.invoke(email)
        _uiState.update { state ->
            state.copy(
                email = state.email.copy(
                    field = email,
                    state = emailValidationState,
                ),
                enabled = emailValidationState.isValid,
            )
        }
    }
}