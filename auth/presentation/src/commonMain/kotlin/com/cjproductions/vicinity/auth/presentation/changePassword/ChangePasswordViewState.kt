package com.cjproductions.vicinity.auth.presentation.changePassword

import com.cjproductions.vicinity.auth.presentation.components.FormField

data class ChangePasswordViewState(
  val password: FormField = FormField(),
  val loading: Boolean = false,
  val enabled: Boolean = false,
)