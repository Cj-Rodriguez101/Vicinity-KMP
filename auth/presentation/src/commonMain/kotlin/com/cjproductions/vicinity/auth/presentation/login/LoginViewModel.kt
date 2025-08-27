package com.cjproductions.vicinity.auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cjproductions.vicinity.auth.domain.AuthError
import com.cjproductions.vicinity.auth.domain.LoginUserWithEmailUseCase
import com.cjproductions.vicinity.auth.domain.LoginUserWithGoogleUseCase
import com.cjproductions.vicinity.auth.domain.validator.IsEmailValidUseCase
import com.cjproductions.vicinity.auth.domain.validator.IsPasswordValidUseCase
import com.cjproductions.vicinity.auth.domain.validator.ResendEmailVerificationUseCase
import com.cjproductions.vicinity.auth.presentation.components.FormField
import com.cjproductions.vicinity.auth.presentation.login.LoginAction.OnAlreadyRegisterClick
import com.cjproductions.vicinity.auth.presentation.login.LoginAction.OnAppleLoginClicked
import com.cjproductions.vicinity.auth.presentation.login.LoginAction.OnEmailEntered
import com.cjproductions.vicinity.auth.presentation.login.LoginAction.OnForgotPasswordClick
import com.cjproductions.vicinity.auth.presentation.login.LoginAction.OnGoogleLoginClicked
import com.cjproductions.vicinity.auth.presentation.login.LoginAction.OnPasswordEntered
import com.cjproductions.vicinity.auth.presentation.login.LoginAction.OnSubmit
import com.cjproductions.vicinity.auth.presentation.login.LoginDestination.Discover
import com.cjproductions.vicinity.auth.presentation.login.LoginDestination.ForgotPassword
import com.cjproductions.vicinity.auth.presentation.login.LoginDestination.Register
import com.cjproductions.vicinity.auth.presentation.login.LoginDestination.Verification
import com.cjproductions.vicinity.core.domain.util.onFailure
import com.cjproductions.vicinity.core.domain.util.onSuccess
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
import vicinity.auth.presentation.generated.resources.failed_to_login

class LoginViewModel(
    private val isEmailValidUseCase: IsEmailValidUseCase,
    private val isPasswordValidUseCase: IsPasswordValidUseCase,
    private val loginUserWithEmail: LoginUserWithEmailUseCase,
    private val loginUserWithGoogle: LoginUserWithGoogleUseCase,
    private val resendEmailVerification: ResendEmailVerificationUseCase,
    private val snackBarController: SnackBarController,
): ViewModel() {

    private val _loginDestination = Channel<LoginDestination>()
    val loginDestination = _loginDestination.receiveAsFlow()

    private val _uiState = MutableStateFlow(LoginViewState())
    val uiState = _uiState.asStateFlow()

    fun onAction(action: LoginAction) {
        when (action) {
            is OnEmailEntered -> updateEmail(action.email)

            is OnPasswordEntered -> updatePassword(action.password)

            OnSubmit -> loginUser()

            OnAlreadyRegisterClick -> goToRegister()

            OnForgotPasswordClick -> goToForgotPassword()

            OnAppleLoginClicked -> {
                //I'm not paying $99 to apple to test login
            }

            OnGoogleLoginClicked -> loginWithGoogle()
        }
    }

    private fun loginWithGoogle() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = loginUserWithGoogle.invoke()
            if (result.isSuccess) {
                _loginDestination.send(Discover)
            } else {
                snackBarController.sendEvent(
                    SnackBarUiEvent(message = StringResourceId(Res.string.failed_to_login))
                )
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    private fun goToForgotPassword() {
        viewModelScope.launch {
            _loginDestination.send(ForgotPassword)
        }
    }

    private fun goToRegister() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    email = FormField(),
                    password = FormField(),
                )
            }
            _loginDestination.send(Register)
        }
    }

    private fun loginUser() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val (email, password) = uiState.value
            val trimmedEmail = email.field.trim()
            loginUserWithEmail.invoke(
                email = trimmedEmail,
                password = password.field.trim(),
            ).onSuccess {
                _loginDestination.send(Discover)
            }.onFailure { authError ->
                if (authError is AuthError.EmailNotConfirmed) {
                    resendEmailVerification.invoke(trimmedEmail)
                    _loginDestination.send(Verification(trimmedEmail))
                } else {
                    snackBarController.sendEvent(
                        SnackBarUiEvent(message = StringResourceId(Res.string.failed_to_login))
                    )
                }
                _uiState.update { it.copy(isLoading = false) }
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
                isSubmitEnabled = !state.isLoading && passwordState.isValid
                        && state.email.state?.isValid == true
            )
        }
    }

    private fun updateEmail(email: String) {
        _uiState.update { state ->
            val emailState = isEmailValidUseCase.invoke(email)

            state.copy(
                email = FormField(
                    field = email,
                    state = emailState,
                ),
                isSubmitEnabled = !state.isLoading && emailState.isValid
                        && state.password.state?.isValid == true
            )
        }
    }
}