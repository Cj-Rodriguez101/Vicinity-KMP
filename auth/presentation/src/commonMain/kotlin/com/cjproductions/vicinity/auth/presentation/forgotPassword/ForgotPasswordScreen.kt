package com.cjproductions.vicinity.auth.presentation.forgotPassword

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cjproductions.vicinity.auth.presentation.components.RadiusAuthTextField
import com.cjproductions.vicinity.auth.presentation.forgotPassword.ForgotPasswordAction.OnBackClick
import com.cjproductions.vicinity.auth.presentation.forgotPassword.ForgotPasswordAction.OnEmailChange
import com.cjproductions.vicinity.auth.presentation.forgotPassword.ForgotPasswordAction.OnSubmitClick
import com.cjproductions.vicinity.auth.presentation.forgotPassword.ForgotPasswordDestination.Back
import com.cjproductions.vicinity.auth.presentation.forgotPassword.ForgotPasswordDestination.Verification
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize
import com.cjproductions.vicinity.core.presentation.ui.components.RadiusButton
import com.cjproductions.vicinity.core.presentation.ui.components.TopNavBarWithBack
import com.cjproductions.vicinity.core.presentation.ui.observeAsEvents
import com.cjproductions.vicinity.core.presentation.ui.theme.VicinityTheme
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import vicinity.auth.presentation.generated.resources.Res
import vicinity.auth.presentation.generated.resources.email
import vicinity.auth.presentation.generated.resources.enter_your_email_address
import vicinity.auth.presentation.generated.resources.forgot_password
import vicinity.auth.presentation.generated.resources.submit

@Composable
fun ForgotPasswordScreenRoot(
  viewModel: ForgotPasswordViewModel = koinViewModel(),
  onBackClick: () -> Unit,
  onVerificationClick: (email: String) -> Unit,
) {
  val state by viewModel.uiState.collectAsStateWithLifecycle()
  viewModel.forgotPasswordDestination.observeAsEvents { destination ->
    when (destination) {
      Back -> onBackClick()
      is Verification -> onVerificationClick(destination.email)
    }
  }
  ForgotPasswordScreen(
    uiState = state,
    onAction = viewModel::onAction,
  )
}

@Composable
private fun ForgotPasswordScreen(
  uiState: ForgotPasswordViewState,
  onAction: (ForgotPasswordAction) -> Unit,
) {
  VicinityTheme {
    Scaffold(
      topBar = {
        TopNavBarWithBack(
          title = stringResource(Res.string.forgot_password),
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
          Text(
            text = stringResource(Res.string.enter_your_email_address),
            style = MaterialTheme.typography.titleMedium,
          )
        }
        item {
          Spacer(modifier = Modifier.height(GlobalPaddingAndSize.Medium.dp))
          with(uiState.email) {
            RadiusAuthTextField(
              label = stringResource(Res.string.email),
              text = field,
              state = state,
              onTextChanged = { onAction(OnEmailChange(it)) },
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
private fun ForgotPasswordScreenPreview() {
  VicinityTheme {
    ForgotPasswordScreen(
      uiState = ForgotPasswordViewState(),
      onAction = {},
    )
  }
}