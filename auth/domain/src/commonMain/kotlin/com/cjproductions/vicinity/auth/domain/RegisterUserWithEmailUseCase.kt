package com.cjproductions.vicinity.auth.domain

import com.cjproductions.vicinity.core.domain.util.Result

class RegisterUserWithEmailUseCase(
  private val authRepository: AuthRepository
) {
  suspend operator fun invoke(
    name: String,
    email: String,
    password: String,
  ): Result<Unit, AuthError> = authRepository.signUpWithPassword(
    username = name,
    email = email,
    password = password,
  )
}