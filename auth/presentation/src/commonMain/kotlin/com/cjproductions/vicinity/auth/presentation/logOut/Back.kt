package com.cjproductions.vicinity.auth.presentation.logOut

sealed interface LogoutDestination {
  data object Back: LogoutDestination
}
