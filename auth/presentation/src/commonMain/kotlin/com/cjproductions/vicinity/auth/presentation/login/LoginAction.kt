package com.cjproductions.vicinity.auth.presentation.login

sealed interface LoginAction {
  data class OnEmailEntered(val email: String): LoginAction
  data class OnPasswordEntered(val password: String): LoginAction
  data object OnSubmit: LoginAction
  data object OnGoogleLoginClicked: LoginAction
  data object OnAppleLoginClicked: LoginAction
  data object OnAlreadyRegisterClick: LoginAction
  data object OnForgotPasswordClick: LoginAction
}