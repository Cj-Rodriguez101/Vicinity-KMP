package com.cjproductions.vicinity.auth.presentation.register

sealed interface RegisterDestination {
  data object Discover: RegisterDestination
  data class Verification(val email: String): RegisterDestination
  data object Login: RegisterDestination
}