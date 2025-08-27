package com.cjproductions.vicinity.auth.presentation.register

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
import com.cjproductions.vicinity.auth.presentation.register.RegisterAction.OnAlreadyLoginClick
import com.cjproductions.vicinity.auth.presentation.register.RegisterAction.OnEmailEntered
import com.cjproductions.vicinity.auth.presentation.register.RegisterAction.OnGoogleLoginClicked
import com.cjproductions.vicinity.auth.presentation.register.RegisterAction.OnNameEntered
import com.cjproductions.vicinity.auth.presentation.register.RegisterAction.OnPasswordEntered
import com.cjproductions.vicinity.auth.presentation.register.RegisterAction.OnSubmit
import com.cjproductions.vicinity.auth.presentation.register.RegisterDestination.Discover
import com.cjproductions.vicinity.auth.presentation.register.RegisterDestination.Verification
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
import vicinity.auth.presentation.generated.resources.already_have_an_account
import vicinity.auth.presentation.generated.resources.create_account
import vicinity.auth.presentation.generated.resources.email
import vicinity.auth.presentation.generated.resources.google
import vicinity.auth.presentation.generated.resources.name
import vicinity.auth.presentation.generated.resources.or_continue_with
import vicinity.auth.presentation.generated.resources.password
import vicinity.auth.presentation.generated.resources.submit
import vicinity.auth.presentation.generated.resources.Res as AuthRes

@Composable
fun RegisterScreenRoot(
  viewModel: RegisterViewModel = koinViewModel(),
  goToVerification: (String) -> Unit,
  goToDiscover: () -> Unit,
  goBack: () -> Unit,
  goToLogin: () -> Unit,
) {
  val state by viewModel.uiState.collectAsStateWithLifecycle()
  viewModel.registerDestination.observeAsEvents { event ->
    when (event) {
      Discover -> goToDiscover()
      is Verification -> goToVerification(event.email)
      else -> Unit
    }
  }

  RegisterScreen(
    state = state,
    onAuthViewAction = { action ->
      when (action) {
        OnAlreadyLoginClick -> goToLogin()
        else -> Unit
      }
      viewModel.onAction(action)
    },
    goBack = goBack,
  )
}

@Composable
fun RegisterScreen(
  state: RegisterViewState,
  onAuthViewAction: (RegisterAction) -> Unit,
  goBack: () -> Unit,
) {
  VicinityTheme {
    Scaffold(
      topBar = {
        TopNavBarWithBack(
          onBackClick = goBack,
          title = stringResource(AuthRes.string.create_account),
        )
      }
    ) { padding ->
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(padding)
      ) {
        RegisterContent(
          state = state,
          onRegisterAction = onAuthViewAction,
        )
      }
    }
  }
}

@Composable
fun RegisterContent(
  state: RegisterViewState,
  onRegisterAction: (RegisterAction) -> Unit,
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
          label = stringResource(AuthRes.string.google),
          image = FontAwesomeIcons.Brands.Google
        ) { onRegisterAction(OnGoogleLoginClicked) }
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
          text = stringResource(AuthRes.string.or_continue_with),
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
          label = stringResource(AuthRes.string.name),
          text = state.username.field,
          state = state.username.state,
          modifier = Modifier.semantics { contentType = ContentType.Username },
          onTextChanged = { onRegisterAction(OnNameEntered(it)) }
        )

        RadiusAuthTextField(
          label = stringResource(AuthRes.string.email),
          text = state.email.field,
          state = state.email.state,
          modifier = Modifier.semantics { contentType = ContentType.EmailAddress },
          onTextChanged = { onRegisterAction(OnEmailEntered(it)) }
        )

        RadiusPasswordTextField(
          label = stringResource(AuthRes.string.password),
          text = state.password.field,
          state = state.password.state,
          modifier = Modifier
            .padding(bottom = Medium.dp)
            .semantics { contentType = ContentType.Password },
          onTextChanged = { onRegisterAction(OnPasswordEntered(it)) }
        )
      }
    }

    item {
      Column(
        verticalArrangement = Arrangement.spacedBy(XSmall.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth(),
      ) {
        PrivacyAndTerms(modifier = Modifier.fillMaxWidth())

        val keyboardController = LocalSoftwareKeyboardController.current

        RadiusButton(
          label = stringResource(AuthRes.string.submit),
          enabled = state.isSubmitEnabled,
          loading = state.isLoading,
        ) { onRegisterAction(OnSubmit) }

        Text(
          text = stringResource(AuthRes.string.already_have_an_account),
          color = MaterialTheme.colorScheme.primary,
          modifier = Modifier
            .padding(bottom = Medium.dp)
            .clickable {
              onRegisterAction(OnAlreadyLoginClick)
              keyboardController?.hide()
            }
            .imePadding(),
          textAlign = TextAlign.Center,
        )
      }

    }
  }
}

@Preview
@Composable
fun RegisterScreenPreview() {
  VicinityTheme {
    RegisterScreen(
      state = RegisterViewState(
        username = FormField(field = "Gloria Valentine"),
        email = FormField(field = "percy.rogers@example.com"),
        password = FormField("faucibus"),
        isLoading = false,
        isSubmitEnabled = false,
      ),
      onAuthViewAction = {},
      goBack = {},
    )
  }
}