package com.cjproductions.vicinity.auth.presentation.changePassword

sealed interface ChangePasswordAction {
  data class OnPasswordChange(val password: String): ChangePasswordAction
  data object OnSubmitClick: ChangePasswordAction
  data object OnBackClick: ChangePasswordAction
}