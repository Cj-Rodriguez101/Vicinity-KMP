package com.cjproductions.vicinity.auth.presentation.login

import com.cjproductions.vicinity.auth.presentation.components.FormField

data class LoginViewState(
  val email: FormField = FormField(),
  val password: FormField = FormField(),
  val isLoading: Boolean = false,
  val isSubmitEnabled: Boolean = false,
)