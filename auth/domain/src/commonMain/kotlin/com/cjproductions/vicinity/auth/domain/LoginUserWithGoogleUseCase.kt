package com.cjproductions.vicinity.auth.domain

import com.cjproductions.vicinity.core.domain.util.Result

class LoginUserWithGoogleUseCase(
  private val authRepository: AuthRepository,
) {
  suspend operator fun invoke(): Result<Unit, AuthError> = authRepository.signInWithGoogle()
}