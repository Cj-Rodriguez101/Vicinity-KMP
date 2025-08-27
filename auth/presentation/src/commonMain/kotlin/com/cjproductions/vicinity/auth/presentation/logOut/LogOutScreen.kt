package com.cjproductions.vicinity.auth.presentation.logOut

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.Large
import com.cjproductions.vicinity.core.presentation.ui.components.BOTTOM_SHEET_HEIGHT
import com.cjproductions.vicinity.core.presentation.ui.components.NegativeRadiusButton
import com.cjproductions.vicinity.core.presentation.ui.components.RadiusButton
import com.cjproductions.vicinity.core.presentation.ui.observeAsEvents
import com.cjproductions.vicinity.core.presentation.ui.theme.VicinityTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel
import vicinity.auth.presentation.generated.resources.are_you_sure_you_want_to_sign_out
import vicinity.auth.presentation.generated.resources.cancel
import vicinity.auth.presentation.generated.resources.continue_to_log_out
import vicinity.auth.presentation.generated.resources.you_will_not_receive_notifications
import vicinity.core.presentation.ui.generated.resources.Close
import vicinity.core.presentation.ui.generated.resources.Res
import vicinity.auth.presentation.generated.resources.Res as AuthRes

@Composable
fun LogOutScreenRoot(
  viewModel: LogOutViewModel = koinViewModel(),
  onDismiss: () -> Unit,
  onLogOut: () -> Unit,
) {
  val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
  viewModel.destination.observeAsEvents { onLogOut() }
  LogOutScreen(
    isLoading = isLoading,
    onLogOut = viewModel::logOut,
    onDismiss = onDismiss,
  )
}

@Composable
fun LogOutScreen(
  isLoading: Boolean,
  onLogOut: () -> Unit,
  onDismiss: () -> Unit,
) {
  VicinityTheme {
    Scaffold(modifier = Modifier.height(BOTTOM_SHEET_HEIGHT)) {
      Column(
        verticalArrangement = Arrangement.spacedBy(GlobalPaddingAndSize.Medium.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(GlobalPaddingAndSize.Medium.dp),
      ) {
        IconButton(
          onClick = onDismiss,
          modifier = Modifier
            .size(Large.dp)
            .align(Alignment.End),
        ) {
          Icon(
            imageVector = vectorResource(Res.drawable.Close),
            contentDescription = null,
            modifier = Modifier
          )
        }

        Text(
          text = stringResource(AuthRes.string.are_you_sure_you_want_to_sign_out),
          fontWeight = FontWeight.Bold,
          style = MaterialTheme.typography.titleMedium,
        )

        Text(
          text = stringResource(AuthRes.string.you_will_not_receive_notifications),
          textAlign = TextAlign.Center,
        )

        RadiusButton(
          label = stringResource(AuthRes.string.continue_to_log_out),
          onClick = onLogOut,
          loading = isLoading
        )

        NegativeRadiusButton(
          label = stringResource(AuthRes.string.cancel),
          onClick = onDismiss,
        )
      }
    }
  }
}

@Preview
@Composable
fun LogOutScreenPreview() {
  LogOutScreen(
    isLoading = false,
    onLogOut = {},
    onDismiss = {}
  )
}