package com.cjproductions.vicinity.auth.presentation.verifyUser

sealed interface VerifyUserAction {
  data class OnCodeChange(val code: String): VerifyUserAction
  data object OnSubmit: VerifyUserAction
  data object OnResendCode: VerifyUserAction
  data object OnBackClick: VerifyUserAction
}