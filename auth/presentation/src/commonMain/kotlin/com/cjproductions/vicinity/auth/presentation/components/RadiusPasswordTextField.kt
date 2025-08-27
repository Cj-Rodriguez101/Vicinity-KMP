package com.cjproductions.vicinity.auth.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import com.cjproductions.vicinity.auth.domain.validator.UserValidationState
import com.cjproductions.vicinity.auth.domain.validator.UserValidationState.PasswordValidationState
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.Large
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.Medium
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.XSmall
import com.cjproductions.vicinity.core.presentation.ui.components.RadiusCard
import compose.icons.FontAwesomeIcons
import compose.icons.LineAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Check
import compose.icons.lineawesomeicons.EyeSlashSolid
import compose.icons.lineawesomeicons.EyeSolid
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import vicinity.auth.presentation.generated.resources.Res
import vicinity.auth.presentation.generated.resources.invalid_password_length
import vicinity.auth.presentation.generated.resources.no_capital_password
import vicinity.auth.presentation.generated.resources.no_password
import vicinity.auth.presentation.generated.resources.no_special_password
import vicinity.core.presentation.ui.generated.resources.Close
import vicinity.core.presentation.ui.generated.resources.Res.drawable

@Composable
fun RadiusPasswordTextField(
    text: String,
    textLink: String? = null,
    onTextChanged: (String) -> Unit,
    state: UserValidationState? = null,
    onTextLinkClick: (() -> Unit)? = null,
    label: String?,
    modifier: Modifier = Modifier,
) {
    var isPasswordVisible by remember { mutableStateOf(false) }
    RadiusAuthTextField(
        text = text,
        textLink = textLink,
        state = state,
        onTextChanged = onTextChanged,
        label = label,
        onTextLinkClick = onTextLinkClick,
        modifier = modifier,
        visualTransformation = VisualTransformation.None.takeIf { isPasswordVisible }
            ?: PasswordVisualTransformation(),
        trailingContent = {
            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                Icon(
                    imageVector = LineAwesomeIcons.EyeSolid.takeIf { isPasswordVisible }
                        ?: LineAwesomeIcons.EyeSlashSolid,
                    contentDescription = "",
                    modifier = Modifier.size(Large.dp)
                )
            }
        },
        hintContent = {
            AnimatedVisibility(visible = state != null && !state.isValid) {
                (state as? PasswordValidationState)?.let {
                    Column {
                        Spacer(modifier = Modifier.height(XSmall.dp))
                        PasswordHint(it)
                    }
                }
            }
        }
    )
}

@Composable
fun PasswordHint(passwordValidationState: PasswordValidationState) {
    RadiusCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Medium.dp),
            verticalArrangement = Arrangement.spacedBy(XSmall.dp)
        ) {
            PasswordHintRow(
                isError = passwordValidationState.isEmpty,
                error = stringResource(Res.string.no_password)
            )

            PasswordHintRow(
                isError = passwordValidationState.isNotMinPasswordLength,
                error = stringResource(Res.string.invalid_password_length)
            )

            PasswordHintRow(
                isError = passwordValidationState.hasNoCapitalLetter,
                error = stringResource(Res.string.no_capital_password)
            )

            PasswordHintRow(
                isError = passwordValidationState.hasNoSpecialCharacter,
                error = stringResource(Res.string.no_special_password)
            )
        }
    }
}

@Composable
fun PasswordHintRow(
    isError: Boolean,
    error: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = XSmall.dp),
        horizontalArrangement = Arrangement.spacedBy(XSmall.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = FontAwesomeIcons.Solid.Check.takeUnless { isError }
                ?: vectorResource(drawable.Close),
            contentDescription = "",
            tint = MaterialTheme.colorScheme.primary.takeUnless { isError }
                ?: MaterialTheme.colorScheme.error,
            modifier = Modifier.size(Large.dp)
        )

        Text(
            text = error,
            color = MaterialTheme.colorScheme.primary.takeUnless { isError }
                ?: MaterialTheme.colorScheme.error,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start,
        )
    }
}

@Preview
@Composable
fun PasswordHintPreview() {
    PasswordHint(
        passwordValidationState = PasswordValidationState(
            isEmpty = false
        )
    )
}