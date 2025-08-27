package com.cjproductions.vicinity.profile.presentation.profile.editProfile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.Medium
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.XXSmall
import com.cjproductions.vicinity.core.presentation.ui.components.RadiusButton
import com.cjproductions.vicinity.core.presentation.ui.components.RadiusCardContainer
import com.cjproductions.vicinity.core.presentation.ui.components.RadiusTextField
import com.cjproductions.vicinity.core.presentation.ui.components.TopNavBarWithBack
import com.cjproductions.vicinity.core.presentation.ui.observeAsEvents
import com.cjproductions.vicinity.core.presentation.ui.theme.VicinityTheme
import com.cjproductions.vicinity.profile.presentation.profile.components.ProfileImage
import com.cjproductions.vicinity.profile.presentation.profile.editProfile.EditProfileAction.OnChangeImage
import com.cjproductions.vicinity.profile.presentation.profile.editProfile.EditProfileAction.OnUpdateBio
import com.cjproductions.vicinity.profile.presentation.profile.editProfile.EditProfileAction.OnUpdateClick
import com.cjproductions.vicinity.profile.presentation.profile.editProfile.EditProfileAction.OnUpdateLocation
import com.cjproductions.vicinity.profile.presentation.profile.editProfile.EditProfileAction.OnUpdateName
import com.cjproductions.vicinity.profile.presentation.profile.editProfile.EditProfileDestination.Back
import com.cjproductions.vicinity.profile.presentation.profile.editProfile.EditProfileDestination.ChangePassword
import com.cjproductions.vicinity.profile.presentation.profile.editProfile.EditProfileDestination.LocationSelector
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Edit
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.FileKitType.Image
import io.github.vinceglb.filekit.dialogs.openFilePicker
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import vicinity.profile.presentation.generated.resources.Res
import vicinity.profile.presentation.generated.resources.bio
import vicinity.profile.presentation.generated.resources.change_password
import vicinity.profile.presentation.generated.resources.edit_profile
import vicinity.profile.presentation.generated.resources.location
import vicinity.profile.presentation.generated.resources.name
import vicinity.profile.presentation.generated.resources.not_set
import vicinity.profile.presentation.generated.resources.update

@Composable
fun EditProfileScreenRoot(
  viewModel: EditProfileViewModel = koinViewModel(),
  onBackClick: () -> Unit,
  onLocationClick: () -> Unit,
  onPasswordChangeClick: () -> Unit,
) {
  val state by viewModel.uiState.collectAsStateWithLifecycle()
  viewModel.destination.observeAsEvents { destination ->
    when (destination) {
      Back -> onBackClick()
      LocationSelector -> onLocationClick()
      ChangePassword -> onPasswordChangeClick()
    }
  }
  EditProfileScreen(
    uiState = state,
    onBackClick = onBackClick,
    onAction = viewModel::onAction,
  )
}

@Composable
private fun EditProfileScreen(
  uiState: EditProfileState,
  onBackClick: () -> Unit,
  onAction: (EditProfileAction) -> Unit,
) {
  VicinityTheme {
    Scaffold(
      contentWindowInsets = WindowInsets.safeDrawing,
      topBar = {
        TopNavBarWithBack(
          title = stringResource(Res.string.edit_profile),
          onBackClick = onBackClick
        )
      },
    ) { padding ->
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Medium.dp),
        modifier = Modifier
          .padding(padding)
          .padding(horizontal = Medium.dp)
          .verticalScroll(state = rememberScrollState())
          .imePadding()
      ) {
        ProfileImage(avatarData = uiState.avatarData) {
          FileKit.openFilePicker(type = Image)?.also { onAction(OnChangeImage(it)) }
        }

        with(uiState.displayName) {
          RadiusTextField(
            text = field,
            label = stringResource(Res.string.name),
            state = state,
            onTextChange = { onAction(OnUpdateName(it)) },
            hintContent = {
              AnimatedVisibility(visible = state != null && !state.isValid) {
                state?.toUiText()?.asString()?.let {
                  Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = XXSmall.dp)
                  )
                }
              }
            }
          )
        }

        Column {
          Text(
            text = stringResource(Res.string.location),
            modifier = Modifier.padding(bottom = XXSmall.dp),
          )
          RadiusCardContainer(
            modifier = Modifier
              .fillMaxWidth()
              .clickable { onAction(OnUpdateLocation) }
          ) {
            Text(text = uiState.location ?: stringResource(Res.string.not_set))
          }
        }

        with(uiState.bio) {
          RadiusTextField(
            text = field,
            label = stringResource(Res.string.bio),
            singleLine = false,
            onTextChange = { onAction(OnUpdateBio(it)) },
            state = state,
            modifier = Modifier.height(70.dp),
            hintContent = {
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
          )
        }

        if (uiState.isPasswordChangeable) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Medium.dp),
            modifier = Modifier.fillMaxWidth()
              .clickable { onAction(EditProfileAction.ChangePassword) }) {
            Icon(
              imageVector = FontAwesomeIcons.Solid.Edit,
              contentDescription = null,
              modifier = Modifier.size(Medium.dp)
            )
            Text(text = stringResource(Res.string.change_password))
          }
        }

        with(uiState) {
          RadiusButton(
            label = stringResource(Res.string.update),
            enabled = enabled,
            loading = loading,
          ) { onAction(OnUpdateClick) }
        }
      }
    }
  }
}

@Preview
@Composable
fun EditProfileScreenPreview() {
  EditProfileScreen(
    uiState = EditProfileState(),
    onBackClick = {},
    onAction = {}
  )
}