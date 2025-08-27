package com.cjproductions.vicinity.auth.presentation.components

import com.cjproductions.vicinity.auth.domain.validator.UserValidationState

data class FormField(
  val field: String = "",
  val state: UserValidationState? = null,
)