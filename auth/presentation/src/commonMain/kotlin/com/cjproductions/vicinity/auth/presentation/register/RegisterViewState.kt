package com.cjproductions.vicinity.auth.presentation.register

import com.cjproductions.vicinity.auth.presentation.components.FormField

data class RegisterViewState(
  val username: FormField = FormField(),
  val email: FormField = FormField(),
  val password: FormField = FormField(),
  val isLoading: Boolean = false,
  val isSubmitEnabled: Boolean = false,
)