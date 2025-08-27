package com.cjproductions.vicinity.auth.domain.validator

class IsPasswordValidUseCase(
  private val userdataValidator: UserdataValidator
) {
  operator fun invoke(password: String) = userdataValidator.isPasswordValid(password.trim())
}