package com.cjproductions.vicinity.profile.data

import com.cjproductions.vicinity.core.domain.util.Result
import com.cjproductions.vicinity.profile.domain.DomainFileStorage
import com.cjproductions.vicinity.profile.domain.FileStorageError
import com.cjproductions.vicinity.profile.domain.FileType
import com.google.firebase.storage.storageMetadata
import dev.gitlive.firebase.storage.FirebaseStorage
import dev.gitlive.firebase.storage.android
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.readBytes
import kotlinx.coroutines.tasks.await
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
      val storageRef = firebaseStorage.android.reference.child("$path/$name")

      val uploadTask = storageRef.putBytes(
        /* bytes = */ file.readBytes().compressImage(),
        /* metadata = */ storageMetadata { contentType = fileType.type },
      )
      val downloadUrl = uploadTask.await().storage.downloadUrl.await().toString()
      Result.Success(downloadUrl)
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
      val storageRef = firebaseStorage.android.reference.child("$path/$name")

      storageRef.delete().await()
      Result.Success(true)
    } catch (ex: Exception) {
      if (ex is CancellationException) throw ex
      Result.Error(FileStorageError.Unknown(ex.message ?: "Unknown error"))
    }
  }
}