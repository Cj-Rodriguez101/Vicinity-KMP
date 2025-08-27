package com.cjproductions.vicinity.auth.domain

import com.cjproductions.vicinity.core.domain.util.Error
import com.cjproductions.vicinity.core.domain.util.Result
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
  suspend fun signUpWithPassword(
    username: String,
    email: String,
    password: String,
  ): Result<Unit, AuthError>

  suspend fun signInWithPassword(
    email: String,
    password: String,
  ): Result<Unit, AuthError>

  suspend fun signInWithGoogle(): Result<Unit, AuthError>

  suspend fun verifySignUpTokenHash(tokenHash: String): Result<Unit, AuthError>
  suspend fun verifyRecoveryTokenHash(tokenHash: String): Result<Unit, AuthError>

  suspend fun logOut(): Result<Unit, AuthError>
  suspend fun resetPassword(email: String): Result<Unit, AuthError>
  suspend fun changePassword(password: String): Result<Unit, AuthError>
  suspend fun resendSignUpVerificationCode(email: String): Result<Unit, AuthError>
  suspend fun resendForgotPasswordVerificationCode(email: String): Result<Unit, AuthError>
  suspend fun setAuthSession(
    accessToken: String,
    refreshToken: String,
    providerToken: String? = null,
    expiresIn: String?,
    expiresAt: String? = null,
  )

  val isLoggedIn: StateFlow<Boolean>
}

sealed class AuthError: Error {
  data class Unknown(val message: String): AuthError()
  data object EmailNotConfirmed: AuthError()
}