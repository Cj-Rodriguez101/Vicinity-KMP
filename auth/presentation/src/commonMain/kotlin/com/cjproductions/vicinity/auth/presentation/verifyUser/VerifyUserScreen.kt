package com.cjproductions.vicinity.auth.presentation.verifyUser

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cjproductions.vicinity.auth.presentation.components.RadiusAuthTextField
import com.cjproductions.vicinity.auth.presentation.verifyUser.VerifyDestination.Back
import com.cjproductions.vicinity.auth.presentation.verifyUser.VerifyDestination.Discover
import com.cjproductions.vicinity.auth.presentation.verifyUser.VerifyUserAction.OnBackClick
import com.cjproductions.vicinity.auth.presentation.verifyUser.VerifyUserAction.OnCodeChange
import com.cjproductions.vicinity.auth.presentation.verifyUser.VerifyUserAction.OnResendCode
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize
import com.cjproductions.vicinity.core.presentation.ui.components.RadiusButton
import com.cjproductions.vicinity.core.presentation.ui.components.TopNavBarWithBack
import com.cjproductions.vicinity.core.presentation.ui.observeAsEvents
import com.cjproductions.vicinity.core.presentation.ui.theme.VicinityTheme
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import vicinity.auth.presentation.generated.resources.Res
import vicinity.auth.presentation.generated.resources.resend
import vicinity.auth.presentation.generated.resources.verification_code
import vicinity.auth.presentation.generated.resources.verify
import vicinity.auth.presentation.generated.resources.verify_user

@Composable
fun VerifyUserScreenRoot(
  viewModel: VerifyUserViewModel = koinViewModel(),
  goToNextScreen: () -> Unit,
  goBack: () -> Unit,
) {
  viewModel.verifyDestination.observeAsEvents {
    when (it) {
      Back -> goBack()
      Discover -> goToNextScreen()
    }
  }
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  VerifyUserScreen(
    uiState = uiState,
    onAction = viewModel::onAction,
  )
}

@Composable
private fun VerifyUserScreen(
  uiState: VerifyUserState,
  onAction: (VerifyUserAction) -> Unit,
) {
  Scaffold(
    topBar = {
      TopNavBarWithBack(
        title = stringResource(Res.string.verify_user),
        onBackClick = { onAction(OnBackClick) }
      )
    }
  ) { padding ->
    LazyColumn(
      verticalArrangement = Arrangement.SpaceBetween,
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = GlobalPaddingAndSize.Medium.dp)
        .padding(padding)
    ) {

      item {
        Column(
          verticalArrangement = Arrangement.spacedBy(GlobalPaddingAndSize.Medium.dp),
          horizontalAlignment = Alignment.CenterHorizontally,
        ) {
          RadiusAuthTextField(
            text = uiState.otpField.field,
            state = uiState.otpField.state,
            label = stringResource(Res.string.verification_code),
            singleLine = false,
            onTextChanged = { text -> onAction(OnCodeChange(text)) },
            modifier = Modifier.height(150.dp)
          )

          uiState.timeElapsed?.let { Text(text = it) }
        }
      }

      item {
        Column(
          verticalArrangement = Arrangement.spacedBy(GlobalPaddingAndSize.Medium.dp),
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier.padding(bottom = GlobalPaddingAndSize.Medium.dp)
        ) {
          RadiusButton(
            label = stringResource(Res.string.verify),
            enabled = uiState.isEnabled,
            loading = uiState.isLoading,
            onClick = { onAction(VerifyUserAction.OnSubmit) },
          )

          Text(
            text = stringResource(Res.string.resend),
            modifier = Modifier
              .imePadding()
              .clickable { onAction(OnResendCode) },
          )
        }
      }
    }
  }
}

@Preview
@Composable
private fun VerifyUserScreenPreview() {
  VicinityTheme {
    VerifyUserScreen(
      uiState = VerifyUserState(),
      onAction = {},
    )
  }
}