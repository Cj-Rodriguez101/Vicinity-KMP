package com.cjproductions.vicinity.profile.domain

import com.cjproductions.vicinity.core.domain.util.Result
import io.github.vinceglb.filekit.PlatformFile
import kotlinx.coroutines.flow.StateFlow

interface ProfileRepository {
    val userProfile: StateFlow<UserProfile?>
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

    suspend fun deleteProfile(userId: String, path: String): Result<Unit, FileStorageError>

    suspend fun updateUserProfile(user: UserProfile): UserProfile?
}