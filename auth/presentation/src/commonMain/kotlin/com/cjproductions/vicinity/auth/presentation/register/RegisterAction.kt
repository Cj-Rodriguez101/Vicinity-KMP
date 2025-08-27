package com.cjproductions.vicinity.auth.presentation.register

sealed interface RegisterAction {
  data class OnNameEntered(val name: String): RegisterAction
  data class OnEmailEntered(val email: String): RegisterAction
  data class OnPasswordEntered(val password: String): RegisterAction
  data object OnSubmit: RegisterAction
  data object OnGoogleLoginClicked: RegisterAction
  data object OnAppleLoginClicked: RegisterAction
  data object OnAlreadyLoginClick: RegisterAction
}