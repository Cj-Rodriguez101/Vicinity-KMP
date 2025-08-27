package com.cjproductions.vicinity.auth.presentation.forgotPassword

sealed interface ForgotPasswordAction {
  data class OnEmailChange(val email: String): ForgotPasswordAction
  data object OnSubmitClick: ForgotPasswordAction
  data object OnBackClick: ForgotPasswordAction
}