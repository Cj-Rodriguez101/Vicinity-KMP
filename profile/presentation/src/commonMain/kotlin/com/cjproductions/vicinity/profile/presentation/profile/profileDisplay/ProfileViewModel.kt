@file:OptIn(ExperimentalCoroutinesApi::class)

package com.cjproductions.vicinity.profile.presentation.profile.profileDisplay

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cjproductions.vicinity.core.domain.util.onFailure
import com.cjproductions.vicinity.core.domain.util.onSuccess
import com.cjproductions.vicinity.core.presentation.designsystem.components.UIText.StringResourceId
import com.cjproductions.vicinity.core.presentation.ui.SnackBarController
import com.cjproductions.vicinity.core.presentation.ui.SnackBarEvent.SnackBarUiEvent
import com.cjproductions.vicinity.likes.domain.LikesRepository
import com.cjproductions.vicinity.location.domain.LocationRepository
import com.cjproductions.vicinity.location.domain.model.getCountry
import com.cjproductions.vicinity.profile.domain.ProfileRepository
import com.cjproductions.vicinity.profile.domain.StoreAndUpdateProfileImageUseCase
import com.cjproductions.vicinity.profile.domain.UserProfile
import com.cjproductions.vicinity.profile.presentation.profile.components.AvatarData
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
import io.github.vinceglb.filekit.PlatformFile
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import vicinity.profile.presentation.generated.resources.Res
import vicinity.profile.presentation.generated.resources.change_image_error

class ProfileViewModel(
  private val profileRepository: ProfileRepository,
  private val storeAndUpdateProfileImage: StoreAndUpdateProfileImageUseCase,
  private val snackBarController: SnackBarController,
  likesRepository: LikesRepository,
  locationRepository: LocationRepository,
): ViewModel() {

  private val userProfile: StateFlow<UserProfile?> = profileRepository.userProfile
  private val _profileDestination = Channel<ProfileDisplayDestination>()
  val profileDestination = _profileDestination.receiveAsFlow()

  private val _profileDisplayState = MutableStateFlow(
    with(profileRepository.userProfile.value) {
      ProfileDisplayState(
        id = this?.id,
        avatarData = AvatarData(
          name = this?.name,
          image = this?.image,
        ),
        name = this?.name,
        bio = this?.bio,
        location = locationRepository.location.value?.getCountry(),
        email = this?.email,
        provider = this?.provider.orEmpty(),
      )
    }
  )

  val uiState = combine(
    _profileDisplayState,
    locationRepository.location,
    userProfile,
    likesRepository.likeCount,
  ) { state, location, userDomain, likeCount ->
    userDomain ?: return@combine state
    with(userDomain) {
      state.copy(
        avatarData = state.avatarData.copy(
          image = image,
          name = name,
        ),
        name = name,
        bio = bio,
        email = email,
        provider = provider,
        location = location?.getCountry(),
        likeCount = likeCount.toInt(),
      )
    }
  }.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = ProfileDisplayState(),
  )

  fun onAction(action: ProfileAction) {
    when (action) {
      is OnChangeImage -> changeImage(action.platformFile)

      OnBackClick -> openDestination(Back)
      OnEditProfileClick -> openDestination(EditProfile)
      OnLikesClick -> openDestination(Likes)
      OnLocationClick -> openDestination(LocationSelector)
      OnLogOut -> openDestination(LogOut)
      OnConfirmDeleteClick -> deleteUser()
    }
  }

  private fun changeImage(platformFile: PlatformFile) {
    viewModelScope.launch {
      _profileDisplayState.update { it.copy(avatarData = it.avatarData.copy(isLoading = true)) }
      storeAndUpdateProfileImage.invoke(
        path = USERS_PATH,
        name = userProfile.value?.id.orEmpty(),
        image = platformFile
      ).getOrNull()?.let { userDomainEntity ->
        _profileDisplayState.update { state ->
          with(userDomainEntity) {
            state.copy(
              avatarData = state.avatarData.copy(image = userDomainEntity.image),
              name = name,
              bio = bio,
              email = email,
              provider = provider,
              location = uiState.value.location,
            )
          }
        }
      } ?: run {
        snackBarController.sendEvent(
          SnackBarUiEvent(StringResourceId(Res.string.change_image_error))
        )
      }
      _profileDisplayState.update { it.copy(avatarData = it.avatarData.copy(isLoading = false)) }
    }
  }

  private fun openDestination(destination: ProfileDisplayDestination) {
    viewModelScope.launch { _profileDestination.send(destination) }
  }

  private fun deleteUser() {
    viewModelScope.launch {
      _profileDisplayState.update { it.copy(loading = true) }

      profileRepository.deleteProfile(
        userId = userProfile.value?.id.orEmpty(),
        path = USERS_PATH
      ).onSuccess {
        _profileDestination.send(Back)
      }.onFailure {
        snackBarController.sendEvent(
          SnackBarUiEvent(StringResourceId(Res.string.change_image_error))
        )
        _profileDisplayState.update { it.copy(loading = false) }
      }
    }
  }
}

internal const val USERS_PATH = "users"