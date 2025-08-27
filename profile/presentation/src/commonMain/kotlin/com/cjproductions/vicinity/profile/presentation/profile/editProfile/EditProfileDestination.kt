package com.cjproductions.vicinity.profile.presentation.profile.editProfile

sealed class EditProfileDestination {
  data object Back: EditProfileDestination()
  data object ChangePassword: EditProfileDestination()
  data object LocationSelector: EditProfileDestination()
}