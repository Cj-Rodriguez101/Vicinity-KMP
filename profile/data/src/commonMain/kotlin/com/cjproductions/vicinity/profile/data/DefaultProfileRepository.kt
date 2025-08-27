@file:OptIn(ExperimentalCoroutinesApi::class)

package com.cjproductions.vicinity.profile.data

import com.cjproductions.vicinity.core.domain.serverless.LifecycleState
import com.cjproductions.vicinity.core.domain.serverless.ServerlessRepository
import com.cjproductions.vicinity.core.domain.util.Result
import com.cjproductions.vicinity.likes.domain.LikesRepository
import com.cjproductions.vicinity.profile.data.user.ProfileUserDataSource
import com.cjproductions.vicinity.profile.data.user.model.toUserDataEntity
import com.cjproductions.vicinity.profile.data.user.model.toUserDomainEntity
import com.cjproductions.vicinity.profile.domain.FileStorageError
import com.cjproductions.vicinity.profile.domain.FileType
import com.cjproductions.vicinity.profile.domain.ProfileRepository
import com.cjproductions.vicinity.profile.domain.UserProfile
import com.cjproductions.vicinity.support.tools.AppDispatcher
import com.cjproductions.vicinity.support.user.UserSettingsDataSource
import io.github.vinceglb.filekit.PlatformFile
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.cancellation.CancellationException

class DefaultProfileRepository(
    private val serverlessRepository: ServerlessRepository,
    private val likesRepository: LikesRepository,
    private val profileImageDataSource: ProfileImageDataSource,
    private val profileUserDataSource: ProfileUserDataSource,
    private val appDispatcher: AppDispatcher,
    private val userSettingsDataSource: UserSettingsDataSource,
): ProfileRepository, CoroutineScope {
    private val _userProfile: MutableStateFlow<UserProfile?> = MutableStateFlow(null)
    override val coroutineContext: CoroutineContext = SupervisorJob() + appDispatcher.io
    override val userProfile = _userProfile.asStateFlow()

    private var profileJob: Job? = null

    private val coroutineExceptionHandler: CoroutineExceptionHandler =
        CoroutineExceptionHandler { _, exception -> exception.printStackTrace() }

    init {
        launch(coroutineExceptionHandler + SupervisorJob()) {
            serverlessRepository.lifecycleState.collectLatest { lifeCycleState ->
                if (lifeCycleState == LifecycleState.PAUSED) {
                    profileJob?.cancel()
                    profileJob = null
                    return@collectLatest
                }
                profileJob = launch {
                    userSettingsDataSource.user.collectLatest { user ->
                        val userId = user?.id
                        if (userId == null) return@collectLatest

                        launch {
                            if (!user.authenticated) return@launch
                            profileUserDataSource.observeUserDataById(userId)
                                .collectLatest { userData ->
                                    _userProfile.update { userData?.toUserDomainEntity() }
                                }
                        }
                    }
                }
            }
        }
    }

    override suspend fun storeImage(
        path: String,
        name: String,
        image: PlatformFile,
        fileType: FileType,
    ): Result<String?, FileStorageError> = withContext(appDispatcher.io) {
        try {
            profileImageDataSource.storeImage(
                path = path,
                name = name,
                image = image,
                fileType = fileType
            )
        } catch (ex: Exception) {
            if (ex is CancellationException) throw ex
            println("Upload failed: ${ex.message}")
            Result.Error(FileStorageError.Unknown(ex.message ?: "Unknown error"))
        }
    }

    override suspend fun deleteImage(
        path: String,
        name: String,
    ): Result<Boolean, FileStorageError> =
        try {
            profileImageDataSource.deleteImage(
                path = path,
                name = name,
            )
        } catch (ex: Exception) {
            if (ex is CancellationException) throw ex
            println("Upload failed: ${ex.message}")
            Result.Error(FileStorageError.Unknown(ex.message ?: "Unknown error"))
        }

    override suspend fun deleteProfile(
        userId: String,
        path: String,
    ): Result<Unit, FileStorageError> {
        return try {
            likesRepository.deleteAllLikes(userId)
            profileUserDataSource.deleteUser(userId).also {
                userSettingsDataSource.updateUser(null)
                profileImageDataSource.deleteImage(
                    path = path,
                    name = userId,
                )
            }
            Result.Success(Unit)
        } catch (ex: Exception) {
            if (ex is CancellationException) throw ex
            println("Upload failed: ${ex.message}")
            Result.Error(FileStorageError.Unknown(ex.message ?: "Unknown error"))
        }
    }


    override suspend fun updateUserProfile(user: UserProfile): UserProfile? {
        return try {
            profileUserDataSource.updateUser(user.toUserDataEntity()).toUserDomainEntity()
        } catch (ex: Exception) {
            if (ex is CancellationException) throw ex
            Result.Error(FileStorageError.Unknown(ex.message ?: "Unknown error"))
            null
        }
    }
}