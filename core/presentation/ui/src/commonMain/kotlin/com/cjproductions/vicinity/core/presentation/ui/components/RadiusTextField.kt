package com.cjproductions.vicinity.core.presentation.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import com.cjproductions.vicinity.core.domain.ValidationState
import com.cjproductions.vicinity.core.presentation.designsystem.components.UIText
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.Medium
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.XSmall
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.XXSmall
import com.cjproductions.vicinity.core.presentation.ui.theme.VicinityTheme

data class DefaultValidationState(
    override val isEmpty: Boolean = true,
): ValidationState {
    override val isValid = !isEmpty
}

@Composable
fun RadiusTextField(
    text: String,
    singleLine: Boolean = true,
    textLink: String? = null,
    hint: String? = null,
    label: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    state: ValidationState? = null,
    colors: TextFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
    ),
    onTextChange: (String) -> Unit,
    onTextLinkClick: (() -> Unit)? = null,
    leadingContent: (@Composable () -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    hintContent: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    Column {
        if (label != null || textLink != null) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth().padding(bottom = XSmall.dp)
            ) {
                label?.let {
                    Text(text = it)
                    Spacer(modifier = Modifier.height(XXSmall.dp))
                }
                textLink?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .focusProperties { canFocus = false }
                            .clickable { onTextLinkClick?.invoke() }
                    )
                }
            }
        }
        val isError by remember { mutableStateOf(state != null && !state.isValid) }
        TextField(
            value = text,
            onValueChange = onTextChange,
            placeholder = { hint?.let { Text(text = it) } },
            colors = colors,
            visualTransformation = visualTransformation,
            shape = MaterialTheme.shapes.medium,
            singleLine = singleLine,
            leadingIcon = leadingContent,
            trailingIcon = trailingContent,
            modifier = modifier.fillMaxWidth(),
        )
        hintContent?.let { it() } ?: run {
            AnimatedVisibility(visible = isError) {
                toUiText()?.asString()?.let {
                    Text(
                        text = it,
                        color = Color.Red,
                        modifier = Modifier.padding(top = XXSmall.dp)
                    )
                }
            }
        }
    }
}

fun toUiText(): UIText? = null

@Preview
@Composable
private fun RadiusTextFieldPreview() {
    VicinityTheme {
        Box(modifier = Modifier.padding(Medium.dp)) {
            RadiusTextField(
                text = "",
                label = "Name",
                state = null,
                onTextChange = {},
                modifier = Modifier,
            )
        }
    }
}