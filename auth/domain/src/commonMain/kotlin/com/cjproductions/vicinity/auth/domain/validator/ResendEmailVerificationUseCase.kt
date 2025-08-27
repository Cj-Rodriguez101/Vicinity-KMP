package com.cjproductions.vicinity.auth.domain.validator

import com.cjproductions.vicinity.auth.domain.AuthError
import com.cjproductions.vicinity.auth.domain.AuthRepository
import com.cjproductions.vicinity.core.domain.util.Result

class ResendEmailVerificationUseCase(
  private val authRepository: AuthRepository,
) {
  suspend fun invoke(email: String): Result<Unit, AuthError> {
    return authRepository.resendSignUpVerificationCode(email)
  }
}