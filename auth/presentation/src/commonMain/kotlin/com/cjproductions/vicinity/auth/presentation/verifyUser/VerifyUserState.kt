package com.cjproductions.vicinity.auth.presentation.verifyUser

import com.cjproductions.vicinity.auth.presentation.components.FormField

data class VerifyUserState(
  val otpField: FormField = FormField(),
  val isEnabled: Boolean = false,
  val isLoading: Boolean = false,
  val timeElapsed: String? = null,
)