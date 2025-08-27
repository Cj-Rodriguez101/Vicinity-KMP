package com.cjproductions.vicinity.profile.data

import com.cjproductions.vicinity.core.domain.util.Result
import com.cjproductions.vicinity.profile.domain.DomainFileStorage
import com.cjproductions.vicinity.profile.domain.FileStorageError
import com.cjproductions.vicinity.profile.domain.FileType
import dev.gitlive.firebase.storage.File
import dev.gitlive.firebase.storage.FirebaseStorage
import dev.gitlive.firebase.storage.FirebaseStorageMetadata
import io.github.vinceglb.filekit.PlatformFile
import kotlin.coroutines.cancellation.CancellationException

actual class PlatformStorage(
  private val firebaseStorage: FirebaseStorage,
): DomainFileStorage {
  actual override suspend fun storeFile(
    path: String,
    name: String,
    file: PlatformFile,
    fileType: FileType,
  ): Result<String?, FileStorageError> {
    return try {
      val storageRef = firebaseStorage.reference.child("$path/$name")

      storageRef.putFile(
        file = File(file.nsUrl),
        metadata = FirebaseStorageMetadata(contentType = fileType.type)
      )
      Result.Success(storageRef.getDownloadUrl())
    } catch (ex: Exception) {
      if (ex is CancellationException) throw ex
      Result.Error(FileStorageError.Unknown(ex.message ?: "Unknown error"))
    }
  }

  override suspend fun deleteFile(
    path: String,
    name: String,
  ): Result<Boolean, FileStorageError> {
    return try {
      val storageRef = firebaseStorage.reference.child("$path/$name")

      storageRef.delete()
      Result.Success(true)
    } catch (ex: Exception) {
      if (ex is CancellationException) throw ex
      Result.Error(FileStorageError.Unknown(ex.message ?: "Unknown error"))
    }
  }
}