package com.cjproductions.vicinity.auth.domain

class ObserveLoggedInStateUseCase(
  authRepository: AuthRepository,
) {
  val isLoggedIn = authRepository.isLoggedIn
}