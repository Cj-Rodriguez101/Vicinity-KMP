package com.cjproductions.vicinity.auth.domain

import com.cjproductions.vicinity.core.domain.util.Result

class LoginUserWithEmailUseCase(
  private val authRepository: AuthRepository,
) {
  suspend operator fun invoke(
    email: String,
    password: String,
  ): Result<Unit, AuthError> = authRepository.signInWithPassword(
    email = email,
    password = password,
  )
}