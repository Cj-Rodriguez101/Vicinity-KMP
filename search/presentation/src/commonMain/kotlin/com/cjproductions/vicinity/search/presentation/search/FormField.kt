package com.cjproductions.vicinity.search.presentation.search

import com.cjproductions.vicinity.core.presentation.ui.components.DefaultValidationState

data class FormField(
  val field: String = "",
  val state: DefaultValidationState? = null,
)