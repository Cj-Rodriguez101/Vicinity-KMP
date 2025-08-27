package com.cjproductions.vicinity.profile.presentation.profile.editProfile

import io.github.vinceglb.filekit.PlatformFile

sealed interface EditProfileAction {
  data class OnChangeImage(val platformFile: PlatformFile): EditProfileAction
  data class OnUpdateName(val name: String): EditProfileAction
  data object OnUpdateLocation: EditProfileAction
  data class OnUpdateBio(val bio: String): EditProfileAction
  data object OnUpdateClick: EditProfileAction
  data object ChangePassword: EditProfileAction
}