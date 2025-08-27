package com.cjproductions.vicinity.auth.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cjproductions.vicinity.auth.domain.LoginUserWithGoogleUseCase
import com.cjproductions.vicinity.auth.domain.RegisterUserWithEmailUseCase
import com.cjproductions.vicinity.auth.domain.validator.IsEmailValidUseCase
import com.cjproductions.vicinity.auth.domain.validator.IsPasswordValidUseCase
import com.cjproductions.vicinity.auth.domain.validator.IsUsernameValidUseCase
import com.cjproductions.vicinity.auth.presentation.components.FormField
import com.cjproductions.vicinity.auth.presentation.register.RegisterAction.OnAlreadyLoginClick
import com.cjproductions.vicinity.auth.presentation.register.RegisterAction.OnAppleLoginClicked
import com.cjproductions.vicinity.auth.presentation.register.RegisterAction.OnEmailEntered
import com.cjproductions.vicinity.auth.presentation.register.RegisterAction.OnGoogleLoginClicked
import com.cjproductions.vicinity.auth.presentation.register.RegisterAction.OnNameEntered
import com.cjproductions.vicinity.auth.presentation.register.RegisterAction.OnPasswordEntered
import com.cjproductions.vicinity.auth.presentation.register.RegisterAction.OnSubmit
import com.cjproductions.vicinity.auth.presentation.register.RegisterDestination.Discover
import com.cjproductions.vicinity.auth.presentation.register.RegisterDestination.Login
import com.cjproductions.vicinity.auth.presentation.register.RegisterDestination.Verification
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
import vicinity.auth.presentation.generated.resources.failed_to_register

class RegisterViewModel(
    private val isUsernameValidUseCase: IsUsernameValidUseCase,
    private val isEmailValidUseCase: IsEmailValidUseCase,
    private val isPasswordValidUseCase: IsPasswordValidUseCase,
    private val registerUserWithEmail: RegisterUserWithEmailUseCase,
    private val loginUserWithGoogle: LoginUserWithGoogleUseCase,
    private val snackBarController: SnackBarController,
): ViewModel() {

    private val _registerDestination = Channel<RegisterDestination>()
    val registerDestination = _registerDestination.receiveAsFlow()

    private val _uiState = MutableStateFlow(RegisterViewState())
    val uiState = _uiState.asStateFlow()

    fun onAction(action: RegisterAction) {
        when (action) {
            is OnNameEntered -> updateName(action.name)

            is OnEmailEntered -> updateEmail(action.email)

            is OnPasswordEntered -> updatePassword(action.password)

            OnSubmit -> registerUser()

            OnAlreadyLoginClick -> goToLogin()
            OnAppleLoginClicked -> {
                //I'm not paying $99 to apple to test login
            }

            OnGoogleLoginClicked -> loginWithGoogle()
        }
    }

    private fun loginWithGoogle() {
        viewModelScope.launch {
            _uiState.update { state -> state.copy(isLoading = true) }
            val result = loginUserWithGoogle()
            if (result.isSuccess) {
                _registerDestination.send(Discover)
            } else {
                snackBarController.sendEvent(SnackBarUiEvent(StringResourceId(Res.string.failed_to_register)))
            }
            _uiState.update { state -> state.copy(isLoading = false) }
        }
    }

    private fun goToLogin() {
        viewModelScope.launch { _registerDestination.send(Login) }
    }

    private fun registerUser() {
        viewModelScope.launch {
            _uiState.update { state -> state.copy(isLoading = true) }
            val (username, email, password) = uiState.value
            val trimmedEmail = email.field.trim()
            val result = registerUserWithEmail(
                name = username.field.trim(),
                email = trimmedEmail,
                password = password.field.trim(),
            )
            _uiState.update { state -> state.copy(isLoading = false) }
            if (result.isSuccess) {
                _registerDestination.send(Verification(trimmedEmail))
            } else {
                snackBarController.sendEvent(SnackBarUiEvent(StringResourceId(Res.string.failed_to_register)))
            }
        }
    }

    private fun updatePassword(password: String) {
        _uiState.update { state ->
            val passwordState = isPasswordValidUseCase.invoke(password)

            state.copy(
                password = FormField(
                    field = password,
                    state = passwordState,
                ),
                isSubmitEnabled = !state.isLoading
                        && passwordState.isValid
                        && state.email.state?.isValid == true
                        && state.username.state?.isValid == true
            )
        }
    }

    private fun updateEmail(email: String) {
        _uiState.update { state ->
            val emailState = isEmailValidUseCase.invoke(email)

            state.copy(
                email = FormField(
                    field = email,
                    state = emailState
                ),
                isSubmitEnabled = !state.isLoading
                        && emailState.isValid
                        && state.username.state?.isValid == true
                        && state.password.state?.isValid == true
            )
        }
    }

    private fun updateName(name: String) {
        _uiState.update { state ->
            val nameState = isUsernameValidUseCase.invoke(name)

            state.copy(
                username = FormField(
                    field = name,
                    state = nameState
                ),
                isSubmitEnabled = !state.isLoading
                        && nameState.isValid
                        && state.email.state?.isValid == true
                        && state.password.state?.isValid == true
            )
        }
    }
}