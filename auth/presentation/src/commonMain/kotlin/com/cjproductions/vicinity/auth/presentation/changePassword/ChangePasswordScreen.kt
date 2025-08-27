package com.cjproductions.vicinity.auth.presentation.changePassword

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cjproductions.vicinity.auth.presentation.changePassword.ChangePasswordAction.OnBackClick
import com.cjproductions.vicinity.auth.presentation.changePassword.ChangePasswordAction.OnPasswordChange
import com.cjproductions.vicinity.auth.presentation.changePassword.ChangePasswordAction.OnSubmitClick
import com.cjproductions.vicinity.auth.presentation.components.RadiusPasswordTextField
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize
import com.cjproductions.vicinity.core.presentation.ui.components.RadiusButton
import com.cjproductions.vicinity.core.presentation.ui.components.TopNavBarWithBack
import com.cjproductions.vicinity.core.presentation.ui.observeAsEvents
import com.cjproductions.vicinity.core.presentation.ui.theme.VicinityTheme
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import vicinity.auth.presentation.generated.resources.Res
import vicinity.auth.presentation.generated.resources.change_password
import vicinity.auth.presentation.generated.resources.password
import vicinity.auth.presentation.generated.resources.submit

@Composable
fun ChangePasswordScreenRoot(
    viewModel: ChangePasswordViewModel = koinViewModel(),
    goBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    viewModel.destination.observeAsEvents { destination -> goBack() }
    ChangePasswordScreen(
        uiState = uiState,
        onAction = viewModel::onAction,
    )
}

@Composable
private fun ChangePasswordScreen(
    uiState: ChangePasswordViewState,
    onAction: (ChangePasswordAction) -> Unit,
) {
    VicinityTheme {
        Scaffold(
            topBar = {
                TopNavBarWithBack(
                    title = stringResource(Res.string.change_password),
                    onBackClick = { onAction(OnBackClick) },
                )
            }
        ) { padding ->
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(GlobalPaddingAndSize.Medium.dp),
                modifier = Modifier.padding(horizontal = GlobalPaddingAndSize.Medium.dp)
                    .padding(padding),
            ) {
                item {
                    Spacer(modifier = Modifier.height(GlobalPaddingAndSize.Medium.dp))
                    with(uiState.password) {
                        RadiusPasswordTextField(
                            label = stringResource(Res.string.password),
                            text = field,
                            state = state,
                            onTextChanged = { onAction(OnPasswordChange(it)) },
                        )
                    }
                }

                item {
                    with(uiState) {
                        RadiusButton(
                            label = stringResource(Res.string.submit),
                            enabled = enabled,
                            loading = loading,
                        ) { onAction(OnSubmitClick) }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun ChangePasswordScreenPreview() {
    VicinityTheme {
        ChangePasswordScreen(
            uiState = ChangePasswordViewState(),
            onAction = {},
        )
    }
}