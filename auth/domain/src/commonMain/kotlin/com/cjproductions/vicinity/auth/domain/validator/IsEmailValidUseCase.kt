package com.cjproductions.vicinity.auth.domain.validator

class IsEmailValidUseCase(
  private val userdataValidator: UserdataValidator
) {
  operator fun invoke(email: String) = userdataValidator.isEmailValid(email.trim())
}