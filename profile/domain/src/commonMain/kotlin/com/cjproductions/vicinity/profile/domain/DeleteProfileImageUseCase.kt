package com.cjproductions.vicinity.profile.domain

import com.cjproductions.vicinity.core.domain.util.Result

class DeleteProfileImageUseCase(
  private val profileRepository: ProfileRepository,
) {
  suspend operator fun invoke(
    path: String,
    name: String,
  ): Result<Boolean, FileStorageError> =
    profileRepository.deleteImage(
      path = path,
      name = name,
    )
}