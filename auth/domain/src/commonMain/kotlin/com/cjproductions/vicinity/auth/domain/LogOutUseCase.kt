package com.cjproductions.vicinity.auth.domain

import com.cjproductions.vicinity.core.domain.util.Result

class LogOutUseCase(
  private val authRepository: AuthRepository,
) {
  suspend operator fun invoke(): Result<Unit, AuthError> {
    return authRepository.logOut()
  }
}