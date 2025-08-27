package com.cjproductions.vicinity.auth.presentation.forgotPassword

sealed class ForgotPasswordDestination {
  data class Verification(val email: String): ForgotPasswordDestination()
  data object Back: ForgotPasswordDestination()
}