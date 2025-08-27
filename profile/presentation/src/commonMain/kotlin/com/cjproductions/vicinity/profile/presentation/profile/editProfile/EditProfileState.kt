package com.cjproductions.vicinity.profile.presentation.profile.editProfile

import com.cjproductions.vicinity.core.domain.ValidationState
import com.cjproductions.vicinity.core.presentation.designsystem.components.UIText
import com.cjproductions.vicinity.profile.presentation.profile.components.AvatarData

data class EditProfileState(
  val avatarData: AvatarData = AvatarData(),
  val location: String? = null,
  val displayName: FormField = FormField(),
  val bio: FormField = FormField(),
  val isPasswordChangeable: Boolean = false,
  val enabled: Boolean = false,
  val loading: Boolean = false,
)

data class FormField(
  val field: String = "",
  val state: EditProfileTextFieldState? = null,
)

class EditProfileTextFieldState(
  override val isEmpty: Boolean = true,
  val isBioTooLarge: Boolean? = false,
): ValidationState {
  override val isValid = !isEmpty && (isBioTooLarge == null || !isBioTooLarge)
}

fun EditProfileTextFieldState.toUiText(): UIText? {
  return when {
    this.isEmpty -> UIText.DynamicString("This field cannot be empty")
    this.isBioTooLarge == true -> UIText.DynamicString("Text is too large")
    else -> null
  }
}
