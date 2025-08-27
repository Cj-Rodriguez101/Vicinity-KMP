package com.cjproductions.vicinity.core.presentation.designsystem.components

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource


sealed interface UIText {
  data class DynamicString(val value: String): UIText
  class StringResourceId(
    val id: StringResource,
    val args: Array<Any> = arrayOf(),
  ): UIText

  @Composable
  fun asString(): String {
    return when (this) {
      is DynamicString -> value
      is StringResourceId -> stringResource(resource = id, formatArgs = args)
    }
  }

  suspend fun getString(): String {
    return when (this) {
      is DynamicString -> value
      is StringResourceId -> org.jetbrains.compose.resources.getString(
        resource = id,
        formatArgs = args
      )
    }
  }
}