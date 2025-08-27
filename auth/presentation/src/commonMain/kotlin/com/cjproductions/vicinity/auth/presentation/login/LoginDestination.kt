package com.cjproductions.vicinity.auth.presentation.login


sealed interface LoginDestination {
  data object Discover: LoginDestination
  data object Register: LoginDestination
  data class Verification(val email: String): LoginDestination
  data object ForgotPassword: LoginDestination
}