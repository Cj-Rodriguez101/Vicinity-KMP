package com.cjproductions.vicinity.auth.presentation.login

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cjproductions.vicinity.auth.presentation.components.FormField
import com.cjproductions.vicinity.auth.presentation.components.PrivacyAndTerms
import com.cjproductions.vicinity.auth.presentation.components.RadiusAuthTextField
import com.cjproductions.vicinity.auth.presentation.components.RadiusPasswordTextField
import com.cjproductions.vicinity.auth.presentation.login.LoginAction.OnAlreadyRegisterClick
import com.cjproductions.vicinity.auth.presentation.login.LoginAction.OnEmailEntered
import com.cjproductions.vicinity.auth.presentation.login.LoginAction.OnForgotPasswordClick
import com.cjproductions.vicinity.auth.presentation.login.LoginAction.OnGoogleLoginClicked
import com.cjproductions.vicinity.auth.presentation.login.LoginAction.OnPasswordEntered
import com.cjproductions.vicinity.auth.presentation.login.LoginAction.OnSubmit
import com.cjproductions.vicinity.auth.presentation.login.LoginDestination.Discover
import com.cjproductions.vicinity.auth.presentation.login.LoginDestination.ForgotPassword
import com.cjproductions.vicinity.auth.presentation.login.LoginDestination.Register
import com.cjproductions.vicinity.auth.presentation.login.LoginDestination.Verification
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.Medium
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.XSmall
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.XXXSmall
import com.cjproductions.vicinity.core.presentation.ui.components.RadiusButton
import com.cjproductions.vicinity.core.presentation.ui.components.TopNavBarWithBack
import com.cjproductions.vicinity.core.presentation.ui.observeAsEvents
import com.cjproductions.vicinity.core.presentation.ui.theme.VicinityTheme
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Brands
import compose.icons.fontawesomeicons.brands.Google
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import vicinity.auth.presentation.generated.resources.Res
import vicinity.auth.presentation.generated.resources.create_new_account
import vicinity.auth.presentation.generated.resources.email
import vicinity.auth.presentation.generated.resources.forgot_password
import vicinity.auth.presentation.generated.resources.google
import vicinity.auth.presentation.generated.resources.login
import vicinity.auth.presentation.generated.resources.or_continue_with
import vicinity.auth.presentation.generated.resources.password
import vicinity.auth.presentation.generated.resources.submit

@Composable
fun LoginScreenRoot(
  viewModel: LoginViewModel = koinViewModel(),
  goToDiscover: () -> Unit,
  goToVerification: (String) -> Unit,
  goBack: () -> Unit,
  goToRegister: () -> Unit,
  goToForgotPassword: () -> Unit,
) {
  val state by viewModel.uiState.collectAsStateWithLifecycle()
  viewModel.loginDestination.observeAsEvents { event ->
    when (event) {
      Register -> goToRegister()
      Discover -> goToDiscover()
      ForgotPassword -> goToForgotPassword()
      is Verification -> goToVerification(event.email)
    }
  }
  LoginScreen(
    state = state,
    onAuthViewAction = viewModel::onAction,
    goBack = goBack,
  )
}

@Composable
fun LoginScreen(
  state: LoginViewState,
  onAuthViewAction: (LoginAction) -> Unit,
  goBack: () -> Unit,
) {
  VicinityTheme {
    Scaffold(
      topBar = {
        TopNavBarWithBack(
          onBackClick = goBack,
          title = stringResource(Res.string.login),
        )
      }
    ) { padding ->
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(padding)
      ) {
        LoginContent(
          state = state,
          onLoginAction = onAuthViewAction,
        )
      }
    }
  }
}

@Composable
fun LoginContent(
  state: LoginViewState,
  onLoginAction: (LoginAction) -> Unit,
) {
  LazyColumn(
    modifier = Modifier.padding(horizontal = Medium.dp)
  ) {
    item {
      Column(
        verticalArrangement = Arrangement.spacedBy(XSmall.dp),
        modifier = Modifier.fillMaxWidth().padding(top = Medium.dp)
      ) {
        RadiusButton(
          label = stringResource(Res.string.google),
          image = FontAwesomeIcons.Brands.Google
        ) { onLoginAction(OnGoogleLoginClicked) }
      }
    }

    item {
      Row(
        horizontalArrangement = Arrangement.spacedBy(
          space = XXXSmall.dp,
          alignment = Alignment.CenterHorizontally,
        ),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(XSmall.dp),
      ) {
        Box(
          modifier = Modifier
            .background(Color.DarkGray)
            .height(XXXSmall.dp)
            .weight(0.3f)
        )
        Text(
          text = stringResource(Res.string.or_continue_with),
          modifier = Modifier.weight(1f),
          textAlign = TextAlign.Center
        )
        Box(
          modifier = Modifier
            .background(Color.DarkGray)
            .height(XXXSmall.dp)
            .weight(0.3f)
        )
      }

    }

    item {
      Column(
        verticalArrangement = Arrangement.spacedBy(Medium.dp),
      ) {
        RadiusAuthTextField(
          label = stringResource(Res.string.email),
          text = state.email.field,
          state = state.email.state,
          modifier = Modifier.semantics { contentType = ContentType.EmailAddress },
          onTextChanged = { onLoginAction(OnEmailEntered(it)) }
        )

        Row {
          RadiusPasswordTextField(
            label = stringResource(Res.string.password),
            text = state.password.field,
            textLink = stringResource(Res.string.forgot_password),
            onTextLinkClick = { onLoginAction(OnForgotPasswordClick) },
            state = state.password.state,
            modifier = Modifier
              .padding(bottom = Medium.dp)
              .semantics { contentType = ContentType.Password },
            onTextChanged = {
              onLoginAction(OnPasswordEntered(it))
            }
          )
        }
      }
    }

    item {
      Column(
        verticalArrangement = Arrangement.spacedBy(XSmall.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
      ) {
        PrivacyAndTerms(modifier = Modifier.fillMaxWidth())

        val keyboardController = LocalSoftwareKeyboardController.current
        RadiusButton(
          label = stringResource(Res.string.submit),
          enabled = state.isSubmitEnabled,
          loading = state.isLoading,
        ) {
          onLoginAction(OnSubmit)
          keyboardController?.hide()
        }

        Text(
          text = stringResource(Res.string.create_new_account),
          modifier = Modifier
            .padding(bottom = Medium.dp)
            .clickable { onLoginAction(OnAlreadyRegisterClick) }.imePadding(),
          textAlign = TextAlign.Center,
          color = MaterialTheme.colorScheme.primary,
        )
      }
    }
  }
}

@Preview
@Composable
private fun LoginScreenPreview() {
  VicinityTheme {
    LoginScreen(
      state = LoginViewState(
        email = FormField(field = "percy.rogers@example.com"),
        password = FormField("faucibus"),
        isLoading = false,
        isSubmitEnabled = false,
      ),
      onAuthViewAction = {},
      goBack = {}
    )
  }
}