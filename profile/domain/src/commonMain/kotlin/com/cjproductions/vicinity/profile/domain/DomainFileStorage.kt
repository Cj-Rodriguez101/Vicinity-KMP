package com.cjproductions.vicinity.profile.domain

import com.cjproductions.vicinity.core.domain.util.Error
import com.cjproductions.vicinity.core.domain.util.Result
import io.github.vinceglb.filekit.PlatformFile

interface DomainFileStorage {
  suspend fun storeFile(
    path: String,
    name: String,
    file: PlatformFile,
    fileType: FileType,
  ): Result<String?, FileStorageError>

  suspend fun deleteFile(
    path: String,
    name: String,
  ): Result<Boolean, FileStorageError>
}

sealed class FileStorageError: Error {
  data class Unknown(val message: String): FileStorageError()
}