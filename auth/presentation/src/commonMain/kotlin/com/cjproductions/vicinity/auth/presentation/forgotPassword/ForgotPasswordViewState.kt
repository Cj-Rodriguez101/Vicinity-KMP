package com.cjproductions.vicinity.auth.presentation.forgotPassword

import com.cjproductions.vicinity.auth.presentation.components.FormField

data class ForgotPasswordViewState(
  val email: FormField = FormField(),
  val loading: Boolean = false,
  val enabled: Boolean = false,
)