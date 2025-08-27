package com.cjproductions.vicinity.auth.data.validator

import com.cjproductions.vicinity.auth.domain.validator.UserValidationState.EmailValidationState
import com.cjproductions.vicinity.auth.domain.validator.UserValidationState.PasswordValidationState
import com.cjproductions.vicinity.auth.domain.validator.UserValidationState.UserNameValidationState
import com.cjproductions.vicinity.auth.domain.validator.UserdataValidator

class DefaultUserdataValidator: UserdataValidator {
    private val emailAddressRegex = Regex(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

    override fun isEmailValid(email: String): EmailValidationState {
        return EmailValidationState(
            isInvalidEmailFormat = !email.matches(emailAddressRegex),
            isEmpty = email.isEmpty()
        )
    }

    private val specialCharRegex = "[!\"#$%&'()*+,-./:;\\\\<=>?@\\[\\]^_`{|}~]".toRegex()
    override fun isPasswordValid(password: String): PasswordValidationState {
        return PasswordValidationState(
            isNotMinPasswordLength = password.length < 7,
            hasNoCapitalLetter = !password.any { it.isUpperCase() },
            hasNoSpecialCharacter = !password.contains(specialCharRegex),
            isEmpty = password.isEmpty(),
        )
    }

    override fun isUsernameValid(name: String) = UserNameValidationState(name.isEmpty())
}