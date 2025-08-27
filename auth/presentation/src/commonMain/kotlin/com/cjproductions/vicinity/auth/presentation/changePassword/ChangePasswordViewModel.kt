package com.cjproductions.vicinity.auth.presentation.changePassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cjproductions.vicinity.auth.domain.AuthRepository
import com.cjproductions.vicinity.auth.domain.validator.IsPasswordValidUseCase
import com.cjproductions.vicinity.auth.presentation.changePassword.ChangePasswordAction.OnBackClick
import com.cjproductions.vicinity.auth.presentation.changePassword.ChangePasswordAction.OnPasswordChange
import com.cjproductions.vicinity.auth.presentation.changePassword.ChangePasswordAction.OnSubmitClick
import com.cjproductions.vicinity.auth.presentation.components.FormField
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
import vicinity.auth.presentation.generated.resources.changed_password_successfully
import vicinity.auth.presentation.generated.resources.failed_to_change_password

class ChangePasswordViewModel(
    private val isPasswordValid: IsPasswordValidUseCase,
    private val authRepository: AuthRepository,
    private val snackBarController: SnackBarController,
): ViewModel() {
    private val _destination: Channel<Unit> = Channel()
    val destination = _destination.receiveAsFlow()

    private val _uiState = MutableStateFlow(ChangePasswordViewState())
    val uiState = _uiState.asStateFlow()

    fun onAction(action: ChangePasswordAction) {
        when (action) {
            is OnPasswordChange -> updatePassword(action.password)

            OnSubmitClick -> changePassword()

            OnBackClick -> goBack()
        }
    }

    private fun goBack() {
        viewModelScope.launch { _destination.send(Unit) }
    }

    private fun changePassword() {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true) }
            val result = authRepository.changePassword(uiState.value.password.field.trim())
            if (result.isSuccess) {
                snackBarController.sendEvent(
                    SnackBarUiEvent(StringResourceId(Res.string.changed_password_successfully))
                )
                _destination.send(Unit)
            } else {
                snackBarController.sendEvent(
                    SnackBarUiEvent(StringResourceId(Res.string.failed_to_change_password))
                )
            }
            _uiState.update { it.copy(loading = false) }
        }
    }

    private fun updatePassword(password: String) {
        val state = isPasswordValid.invoke(password)
        _uiState.update {
            it.copy(
                password = FormField(
                    field = password,
                    state = state,
                ),
                enabled = state.isValid,
            )
        }
    }
}