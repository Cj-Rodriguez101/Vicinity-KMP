package com.cjproductions.vicinity.auth.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import com.cjproductions.vicinity.auth.domain.validator.UserValidationState
import com.cjproductions.vicinity.auth.domain.validator.UserValidationState.EmailValidationState
import com.cjproductions.vicinity.auth.domain.validator.UserValidationState.PasswordValidationState
import com.cjproductions.vicinity.auth.domain.validator.UserValidationState.UserNameValidationState
import com.cjproductions.vicinity.core.presentation.designsystem.components.UIText
import com.cjproductions.vicinity.core.presentation.designsystem.components.UIText.StringResourceId
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.Medium
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.XXSmall
import com.cjproductions.vicinity.core.presentation.ui.components.RadiusTextField
import vicinity.auth.presentation.generated.resources.Res.string
import vicinity.auth.presentation.generated.resources.invalid_email
import vicinity.auth.presentation.generated.resources.invalid_password_length
import vicinity.auth.presentation.generated.resources.no_capital_password
import vicinity.auth.presentation.generated.resources.no_email
import vicinity.auth.presentation.generated.resources.no_password
import vicinity.auth.presentation.generated.resources.no_special_password
import vicinity.auth.presentation.generated.resources.no_user_name
import vicinity.auth.presentation.generated.resources.verification_code_should_not_be_empty

@Composable
fun RadiusAuthTextField(
    text: String,
    label: String?,
    state: UserValidationState? = null,
    textLink: String? = null,
    singleLine: Boolean = true,
    onTextChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onTextLinkClick: (() -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    hintContent: (@Composable () -> Unit)? = null,
) {
    RadiusTextField(
        text = text,
        textLink = textLink,
        label = label,
        state = state,
        singleLine = singleLine,
        onTextChange = onTextChanged,
        visualTransformation = visualTransformation,
        trailingContent = trailingContent,
        onTextLinkClick = onTextLinkClick,
        hintContent = {
            hintContent?.let { it() } ?: run {
                AnimatedVisibility(visible = state != null && !state.isValid) {
                    state?.toUiText()?.asString()?.let {
                        Text(
                            text = it,
                            color = Color.Red,
                            modifier = Modifier.padding(top = XXSmall.dp)
                        )
                    }
                }
            }
        },
        modifier = modifier,
    )
}

@Preview
@Composable
fun RadiusAuthTextFieldPreview() {
    MaterialTheme {
        Box(modifier = Modifier.padding(Medium.dp)) {
            RadiusAuthTextField(
                text = "John",
                label = "Name",
                state = null,
                onTextChanged = {},
                modifier = Modifier,
            )
        }
    }
}

fun UserValidationState.toUiText(): UIText? {
    return when (this) {
        is EmailValidationState -> {
            when {
                isEmpty -> StringResourceId(id = string.no_email)

                isInvalidEmailFormat -> StringResourceId(id = string.invalid_email)

                else -> null
            }
        }

        is PasswordValidationState -> {
            when {
                isEmpty -> StringResourceId(id = string.no_password)

                isNotMinPasswordLength -> StringResourceId(id = string.invalid_password_length)

                hasNoCapitalLetter -> StringResourceId(id = string.no_capital_password)

                hasNoSpecialCharacter -> StringResourceId(id = string.no_special_password)

                else -> null
            }
        }

        is UserNameValidationState -> {
            when {
                isEmpty -> StringResourceId(id = string.no_user_name)

                else -> null
            }
        }

        is UserValidationState.VerificationCodeValidationState -> {
            when {
                isEmpty -> StringResourceId(id = string.verification_code_should_not_be_empty)

                else -> null
            }
        }
    }
}