package com.cjproductions.vicinity.profile.presentation.profile.profileDisplay

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.XSmall
import com.cjproductions.vicinity.core.presentation.ui.components.HeaderContent
import com.cjproductions.vicinity.core.presentation.ui.components.NegativeRadiusButton
import com.cjproductions.vicinity.core.presentation.ui.components.RadiusButton
import com.cjproductions.vicinity.core.presentation.ui.components.RadiusCardContainer
import com.cjproductions.vicinity.core.presentation.ui.components.TopNavBarWithBack
import com.cjproductions.vicinity.core.presentation.ui.theme.VicinityTheme
import com.cjproductions.vicinity.profile.presentation.profile.components.ProfileImage
import com.cjproductions.vicinity.profile.presentation.profile.profileDisplay.ProfileAction.OnBackClick
import com.cjproductions.vicinity.profile.presentation.profile.profileDisplay.ProfileAction.OnChangeImage
import com.cjproductions.vicinity.profile.presentation.profile.profileDisplay.ProfileAction.OnConfirmDeleteClick
import com.cjproductions.vicinity.profile.presentation.profile.profileDisplay.ProfileAction.OnEditProfileClick
import com.cjproductions.vicinity.profile.presentation.profile.profileDisplay.ProfileAction.OnLikesClick
import com.cjproductions.vicinity.profile.presentation.profile.profileDisplay.ProfileAction.OnLocationClick
import com.cjproductions.vicinity.profile.presentation.profile.profileDisplay.ProfileAction.OnLogOut
import com.cjproductions.vicinity.profile.presentation.profile.profileDisplay.ProfileDisplayDestination.Back
import com.cjproductions.vicinity.profile.presentation.profile.profileDisplay.ProfileDisplayDestination.EditProfile
import com.cjproductions.vicinity.profile.presentation.profile.profileDisplay.ProfileDisplayDestination.Likes
import com.cjproductions.vicinity.profile.presentation.profile.profileDisplay.ProfileDisplayDestination.LocationSelector
import com.cjproductions.vicinity.profile.presentation.profile.profileDisplay.ProfileDisplayDestination.LogOut
import com.cjproductions.vicinity.profile.presentation.profile.profileDisplay.components.ConfirmDeleteDialog
import com.valentinilk.shimmer.shimmer
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.FileKitType.Image
import io.github.vinceglb.filekit.dialogs.openFilePicker
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import vicinity.core.presentation.ui.generated.resources.privacy_url
import vicinity.core.presentation.ui.generated.resources.terms_url
import vicinity.profile.presentation.generated.resources.Res
import vicinity.profile.presentation.generated.resources.country
import vicinity.profile.presentation.generated.resources.delete_account
import vicinity.profile.presentation.generated.resources.edit_profile
import vicinity.profile.presentation.generated.resources.legal
import vicinity.profile.presentation.generated.resources.location
import vicinity.profile.presentation.generated.resources.log_out
import vicinity.profile.presentation.generated.resources.my_likes
import vicinity.profile.presentation.generated.resources.not_set
import vicinity.profile.presentation.generated.resources.preferences
import vicinity.profile.presentation.generated.resources.privacy_policy
import vicinity.profile.presentation.generated.resources.profile
import vicinity.profile.presentation.generated.resources.terms_of_service
import vicinity.core.presentation.ui.generated.resources.Res as CoreRes

@Composable
fun ProfileScreenRoot(
  viewModel: ProfileViewModel = koinViewModel(),
  onBackClick: () -> Unit,
  onEditProfileClick: () -> Unit,
  onLogOutClick: () -> Unit,
  onLikesClick: () -> Unit,
  onLocationClick: () -> Unit,
) {
  val profileUi by viewModel.uiState.collectAsStateWithLifecycle()
  LaunchedEffect(Unit) {
    viewModel.profileDestination.collectLatest { destination ->
      when (destination) {
        Back -> onBackClick()
        EditProfile -> onEditProfileClick()
        LogOut -> onLogOutClick()
        Likes -> onLikesClick()
        LocationSelector -> onLocationClick()

      }
    }
  }
  ProfileScreen(
    profileDisplayState = profileUi,
    onViewAction = viewModel::onAction,
  )
}

@Composable
private fun ProfileScreen(
  profileDisplayState: ProfileDisplayState,
  onViewAction: (ProfileAction) -> Unit,
) {
  VicinityTheme {
    var shouldShowDialog by remember { mutableStateOf(false) }
    Scaffold(
      contentWindowInsets = WindowInsets.safeDrawing,
      topBar = {
        TopNavBarWithBack(
          title = stringResource(Res.string.profile),
          onBackClick = { onViewAction(OnBackClick) }
        )
      },
      bottomBar = {
        if (shouldShowDialog) {
          ConfirmDeleteDialog(
            loading = profileDisplayState.loading,
            onDelete = { onViewAction(OnConfirmDeleteClick) },
            onDismiss = { shouldShowDialog = false },
          )
        }
      }
    ) { padding ->
      LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(padding),
        contentPadding = PaddingValues(
          start = GlobalPaddingAndSize.Medium.dp,
          end = GlobalPaddingAndSize.Medium.dp,
          bottom = GlobalPaddingAndSize.Medium.dp,
        ),
      ) {
        item {
          Column(
            verticalArrangement = Arrangement.spacedBy(GlobalPaddingAndSize.Medium.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
          ) {
            ProfileImage(avatarData = profileDisplayState.avatarData) {
              FileKit.openFilePicker(type = Image)?.also {
                onViewAction(OnChangeImage(it))
              }
            }

            Column(
              verticalArrangement = Arrangement.spacedBy(XSmall.dp),
              horizontalAlignment = Alignment.CenterHorizontally,
            ) {
              if (!profileDisplayState.name.isNullOrEmpty() && !profileDisplayState.email.isNullOrEmpty()) {
                Text(
                  text = profileDisplayState.name,
                  style = MaterialTheme.typography.titleLarge,
                  fontWeight = FontWeight.Black,
                  fontSize = 16.sp,
                )

                Text(
                  text = profileDisplayState.email,
                  style = MaterialTheme.typography.bodyMedium,
                  fontSize = 16.sp,
                )

                profileDisplayState.bio?.let { bio ->
                  Text(
                    text = bio,
                    style = MaterialTheme.typography.bodyMedium.copy(
                      fontStyle = FontStyle.Italic,
                      fontWeight = FontWeight.Medium,
                    ),
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                  )
                }
              } else {
                repeat(3) {
                  Box(
                    modifier = Modifier.fillMaxWidth()
                      .height(GlobalPaddingAndSize.Medium.dp).shimmer()
                  )
                }
              }
            }

            RadiusButton(stringResource(Res.string.edit_profile)) {
              onViewAction(OnEditProfileClick)
            }
          }
        }

        item {
          Column(
            verticalArrangement = Arrangement.spacedBy(GlobalPaddingAndSize.Medium.dp),
            modifier = Modifier.fillMaxWidth()
              .padding(vertical = GlobalPaddingAndSize.Medium.dp)
          ) {
            HeaderContent(stringResource(Res.string.preferences))
            RadiusCardContainer(
              modifier = Modifier.clickable { onViewAction(OnLikesClick) }
            ) {
              Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
              ) {
                Text(stringResource(Res.string.my_likes))
                if (profileDisplayState.likeCount > 0) {
                  Text(
                    text = profileDisplayState.likeCount.toString(),
                    fontWeight = FontWeight.Black,
                  )
                }
              }
            }
          }
        }

        item {
          Column(
            verticalArrangement = Arrangement.spacedBy(GlobalPaddingAndSize.Medium.dp),
            modifier = Modifier.fillMaxWidth()
              .padding(vertical = GlobalPaddingAndSize.Medium.dp)
          ) {
            HeaderContent(stringResource(Res.string.location))
            RadiusCardContainer(
              modifier = Modifier.clickable { onViewAction(OnLocationClick) }
            ) {
              Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
              ) {
                Text(stringResource(Res.string.country))
                Text(
                  text = profileDisplayState.location
                    ?: stringResource(Res.string.not_set),
                  fontWeight = FontWeight.Black,
                )
              }
            }
          }
        }

        item {
          Column(
            modifier = Modifier.fillMaxWidth()
              .padding(vertical = GlobalPaddingAndSize.Medium.dp)
          ) {
            val uriHandler = LocalUriHandler.current
            val termsUrl = stringResource(CoreRes.string.terms_url)
            val privacyUrl = stringResource(CoreRes.string.privacy_url)
            HeaderContent(stringResource(Res.string.legal))
            Spacer(modifier = Modifier.height(GlobalPaddingAndSize.Medium.dp))
            RadiusCardContainer(
              modifier = Modifier
                .fillMaxWidth()
                .clickable { uriHandler.openUri(termsUrl) }

            ) {
              Text(text = stringResource(Res.string.terms_of_service))
            }
            Spacer(modifier = Modifier.height(XSmall.dp))
            RadiusCardContainer(
              modifier = Modifier
                .fillMaxWidth()
                .clickable { uriHandler.openUri(privacyUrl) }
            ) {
              Text(text = stringResource(Res.string.privacy_policy))
            }
          }
        }

        item {
          Column(verticalArrangement = Arrangement.spacedBy(XSmall.dp)) {
            RadiusButton(stringResource(Res.string.log_out)) { onViewAction(OnLogOut) }
            NegativeRadiusButton(
              label = stringResource(Res.string.delete_account),
              loading = profileDisplayState.loading,
              onClick = { shouldShowDialog = true }
            )
          }
        }
      }
    }
  }
}

@Preview
@Composable
fun ProfileScreenPreview() {
  VicinityTheme {
    ProfileScreen(
      profileDisplayState = ProfileDisplayState(
        id = "1234",
        name = "John james",
        email = "johnboscon@gmail.com",
        bio = "Live to tell the world",
        provider = ""
      ),
      onViewAction = {},
    )
  }
}
