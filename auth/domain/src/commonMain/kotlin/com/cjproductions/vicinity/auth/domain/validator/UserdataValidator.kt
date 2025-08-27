package com.cjproductions.vicinity.auth.domain.validator

import com.cjproductions.vicinity.core.domain.ValidationState

interface UserdataValidator {
  fun isEmailValid(email: String): UserValidationState.EmailValidationState
  fun isUsernameValid(name: String): UserValidationState.UserNameValidationState
  fun isPasswordValid(password: String): UserValidationState.PasswordValidationState
}

sealed interface UserValidationState: ValidationState {
  data class PasswordValidationState(
    val isNotMinPasswordLength: Boolean = true,
    val hasNoCapitalLetter: Boolean = true,
    val hasNoSpecialCharacter: Boolean = true,
    override val isEmpty: Boolean = true,
  ): UserValidationState {
    override val isValid: Boolean =
      !isNotMinPasswordLength && !hasNoCapitalLetter && !hasNoSpecialCharacter && !isEmpty
  }

  data class EmailValidationState(
    val isInvalidEmailFormat: Boolean = true,
    override val isEmpty: Boolean = true,
  ): UserValidationState {
    override val isValid = !isInvalidEmailFormat && !isEmpty
  }

  data class UserNameValidationState(override val isEmpty: Boolean = true): UserValidationState {
    override val isValid: Boolean = !isEmpty
  }

  data class VerificationCodeValidationState(
    override val isEmpty: Boolean = true,
  ): UserValidationState {
    override val isValid: Boolean = !isEmpty
  }
}
