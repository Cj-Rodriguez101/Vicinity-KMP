package com.cjproductions.vicinity.profile.presentation.profile.profileDisplay

import io.github.vinceglb.filekit.PlatformFile

sealed interface ProfileAction {
  data class OnChangeImage(val platformFile: PlatformFile): ProfileAction
  data object OnLogOut: ProfileAction
  data object OnBackClick: ProfileAction
  data object OnEditProfileClick: ProfileAction
  data object OnLikesClick: ProfileAction
  data object OnLocationClick: ProfileAction
  data object OnConfirmDeleteClick: ProfileAction
}