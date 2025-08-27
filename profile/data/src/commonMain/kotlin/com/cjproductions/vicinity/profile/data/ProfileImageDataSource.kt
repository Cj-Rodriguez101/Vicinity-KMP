package com.cjproductions.vicinity.profile.data

import com.cjproductions.vicinity.core.domain.util.Result
import com.cjproductions.vicinity.profile.domain.FileStorageError
import com.cjproductions.vicinity.profile.domain.FileType
import io.github.vinceglb.filekit.PlatformFile

interface ProfileImageDataSource {
  suspend fun storeImage(
    path: String,
    name: String,
    image: PlatformFile,
    fileType: FileType,
  ): Result<String?, FileStorageError>

  suspend fun deleteImage(
    path: String,
    name: String,
  ): Result<Boolean, FileStorageError>
}

class DefaultProfileImageDataSource(
  val platformStorage: PlatformStorage,
): ProfileImageDataSource {
  override suspend fun storeImage(
    path: String,
    name: String,
    image: PlatformFile,
    fileType: FileType,
  ): Result<String?, FileStorageError> =
    platformStorage.storeFile(
      path = path,
      name = name,
      file = image,
      fileType = fileType,
    )

  override suspend fun deleteImage(
    path: String,
    name: String,
  ) = platformStorage.deleteFile(
    path = path,
    name = name,
  )
}