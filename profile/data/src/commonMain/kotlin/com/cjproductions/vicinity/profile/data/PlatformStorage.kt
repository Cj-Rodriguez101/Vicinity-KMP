package com.cjproductions.vicinity.profile.data

import com.cjproductions.vicinity.core.domain.util.Result
import com.cjproductions.vicinity.profile.domain.DomainFileStorage
import com.cjproductions.vicinity.profile.domain.FileStorageError
import com.cjproductions.vicinity.profile.domain.FileType
import io.github.vinceglb.filekit.PlatformFile

expect class PlatformStorage: DomainFileStorage {
  override suspend fun storeFile(
    path: String,
    name: String,
    file: PlatformFile,
    fileType: FileType,
  ): Result<String?, FileStorageError>
}