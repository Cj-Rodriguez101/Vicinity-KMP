package com.cjproductions.vicinity.profile.presentation.profile.profileDisplay

sealed interface ProfileDisplayDestination {
  data object Back: ProfileDisplayDestination
  data object EditProfile: ProfileDisplayDestination
  data object LogOut: ProfileDisplayDestination
  data object Likes: ProfileDisplayDestination
  data object LocationSelector: ProfileDisplayDestination
}