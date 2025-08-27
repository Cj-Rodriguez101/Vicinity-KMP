package com.cjproductions.vicinity.profile.domain

import com.cjproductions.vicinity.core.domain.util.Error
import com.cjproductions.vicinity.core.domain.util.Result
import io.github.vinceglb.filekit.PlatformFile
import kotlinx.datetime.Clock

class StoreAndUpdateProfileImageUseCase(
  private val profileRepository: ProfileRepository,
) {
  suspend operator fun invoke(
    path: String,
    name: String,
    image: PlatformFile,
  ): Result<UserProfile, ImageError> {
    val result = profileRepository.storeImage(
      path = path,
      name = name,
      image = image,
      fileType = FileType.STATIC_IMAGE,
    )

    val userDomain = profileRepository.userProfile.value
      ?: return Result.Error(ImageError.Unknown("User not found"))
    return result.getOrNull()?.let { image ->
      profileRepository.updateUserProfile(
        user = userDomain.copy(
          image = image,
          updatedAt = Clock.System.now().toEpochMilliseconds().toDouble(),
        )
      )?.let { Result.Success(it) } ?: run {
        Result.Error(ImageError.Unknown("Error updating user profile"))
      }
    } ?: run {
      Result.Error(ImageError.Unknown("Error storing image"))
    }
  }
}

sealed class ImageError: Error {
  data class Unknown(val message: String): ImageError()
}