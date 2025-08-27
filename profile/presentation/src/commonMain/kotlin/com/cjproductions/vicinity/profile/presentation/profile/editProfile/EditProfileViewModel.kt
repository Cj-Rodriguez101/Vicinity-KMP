package com.cjproductions.vicinity.profile.presentation.profile.editProfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cjproductions.vicinity.core.presentation.designsystem.components.UIText.StringResourceId
import com.cjproductions.vicinity.core.presentation.ui.SnackBarController
import com.cjproductions.vicinity.core.presentation.ui.SnackBarEvent.SnackBarUiEvent
import com.cjproductions.vicinity.location.domain.LocationRepository
import com.cjproductions.vicinity.location.domain.model.getCountry
import com.cjproductions.vicinity.profile.domain.ProfileRepository
import com.cjproductions.vicinity.profile.domain.StoreAndUpdateProfileImageUseCase
import com.cjproductions.vicinity.profile.domain.UserProfile
import com.cjproductions.vicinity.profile.presentation.profile.components.AvatarData
import com.cjproductions.vicinity.profile.presentation.profile.editProfile.EditProfileAction.ChangePassword
import com.cjproductions.vicinity.profile.presentation.profile.editProfile.EditProfileAction.OnChangeImage
import com.cjproductions.vicinity.profile.presentation.profile.editProfile.EditProfileAction.OnUpdateBio
import com.cjproductions.vicinity.profile.presentation.profile.editProfile.EditProfileAction.OnUpdateClick
import com.cjproductions.vicinity.profile.presentation.profile.editProfile.EditProfileAction.OnUpdateLocation
import com.cjproductions.vicinity.profile.presentation.profile.editProfile.EditProfileAction.OnUpdateName
import com.cjproductions.vicinity.profile.presentation.profile.editProfile.EditProfileDestination.LocationSelector
import com.cjproductions.vicinity.profile.presentation.profile.profileDisplay.USERS_PATH
import io.github.vinceglb.filekit.PlatformFile
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

class EditProfileViewModel(
  private val storeAndUpdateProfileImage: StoreAndUpdateProfileImageUseCase,
  private val profileRepository: ProfileRepository,
  private val locationRepository: LocationRepository,
  private val snackBarController: SnackBarController,
): ViewModel() {

  private val userDomain: StateFlow<UserProfile?> = profileRepository.userProfile
  private val _destination = Channel<EditProfileDestination>()
  val destination = _destination.receiveAsFlow()

  private val _uiState = MutableStateFlow(
    with(userDomain.value) {
      EditProfileState(
        avatarData = AvatarData(
          name = this?.name.orEmpty(),
          image = this?.image,
        ),
        displayName = FormField(field = this?.name.orEmpty()),
        location = locationRepository.location.value?.getCountry(),
        bio = FormField(field = this?.bio.orEmpty()),
      )
    }
  )

  val uiState =
    combine(_uiState, locationRepository.location, userDomain) { state, location, userDomain ->
      state.copy(
        avatarData = state.avatarData.copy(image = userDomain?.image),
        location = location?.getCountry(),
        isPasswordChangeable = userDomain?.provider == "Email"
      )
    }.stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = EditProfileState(),
    )

  fun onAction(action: EditProfileAction) {
    when (action) {
      is OnUpdateName -> updateName(action.name)

      is OnUpdateLocation -> goToLocationSelector()

      is OnUpdateBio -> updateBio(action.bio)

      is OnUpdateClick -> updateUserProfile()

      is OnChangeImage -> updateProfileImage(action.platformFile)


      ChangePassword -> goToChangePassword()
    }
  }

  private fun updateName(name: String) {
    _uiState.update {
      it.copy(
        displayName = _uiState.value.displayName.copy(
          field = name,
          state = EditProfileTextFieldState(name.isEmpty())
        ),
        enabled = name.isNotEmpty(),
      )
    }
  }

  private fun goToLocationSelector() {
    viewModelScope.launch { _destination.send(LocationSelector) }
  }

  private fun updateBio(bio: String) {
    _uiState.update {
      it.copy(
        bio = _uiState.value.bio.copy(
          field = bio,
          state = EditProfileTextFieldState(
            isEmpty = false,
            isBioTooLarge = bio.length > 200,
          ),
        ),
        enabled = uiState.value.displayName.field.isNotEmpty() && _uiState.value.bio.state?.isValid == true
      )
    }
  }

  private fun updateUserProfile() {
    viewModelScope.launch {
      _uiState.update { it.copy(loading = true) }
      val user = profileRepository.userProfile.value
      if (user == null) return@launch
      profileRepository.updateUserProfile(
        user = user.copy(
          name = _uiState.value.displayName.field.trim(),
          bio = _uiState.value.bio.field.trim().takeIf { it.isNotEmpty() },
        )
      )
      _destination.send(EditProfileDestination.Back)
    }
  }

  private fun updateProfileImage(platformFile: PlatformFile) {
    viewModelScope.launch {
      _uiState.update { it.copy(avatarData = it.avatarData.copy(isLoading = true)) }
      storeAndUpdateProfileImage.invoke(
        path = USERS_PATH,
        name = userDomain.value?.id.orEmpty(),
        image = platformFile,
      ).getOrNull()?.let { userDomainEntity ->
        _uiState.update { state ->
          state.copy(avatarData = state.avatarData.copy(image = userDomainEntity.image))
        }
      } ?: run {
        snackBarController.sendEvent(
          SnackBarUiEvent(StringResourceId(Res.string.change_image_error))
        )
      }
      _uiState.update { it.copy(avatarData = it.avatarData.copy(isLoading = false)) }
    }
  }

  private fun goToChangePassword() {
    viewModelScope.launch {
      _destination.send(EditProfileDestination.ChangePassword)
    }
  }
}