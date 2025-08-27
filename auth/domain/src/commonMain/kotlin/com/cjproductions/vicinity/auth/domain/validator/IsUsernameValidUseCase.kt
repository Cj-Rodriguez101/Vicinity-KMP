package com.cjproductions.vicinity.auth.domain.validator

class IsUsernameValidUseCase(
  private val userdataValidator: UserdataValidator
) {
  operator fun invoke(username: String) = userdataValidator.isUsernameValid(username.trim())
}