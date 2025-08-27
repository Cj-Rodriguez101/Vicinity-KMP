@file:OptIn(ExperimentalAtomicApi::class, ExperimentalCoroutinesApi::class)

package com.cjproductions.vicinity.likes.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import app.cash.sqldelight.paging3.QueryPagingSource
import com.cjproductions.vicinity.core.domain.serverless.LifecycleState
import com.cjproductions.vicinity.core.domain.serverless.ServerlessRepository
import com.cjproductions.vicinity.core.domain.util.EmptyResult
import com.cjproductions.vicinity.core.domain.util.Result
import com.cjproductions.vicinity.core.domain.util.asEmptyDataResult
import com.cjproductions.vicinity.likes.data.local.LocalLikesDataSource
import com.cjproductions.vicinity.likes.data.remote.RemoteLikesDataSource
import com.cjproductions.vicinity.likes.data.remote.model.LikeDataEntity
import com.cjproductions.vicinity.likes.data.remote.model.toLikeDataEntity
import com.cjproductions.vicinity.likes.data.remote.model.toLikes
import com.cjproductions.vicinity.likes.domain.DatabaseError
import com.cjproductions.vicinity.likes.domain.LikesRepository
import com.cjproductions.vicinity.likes.domain.model.Like
import com.cjproductions.vicinity.support.tools.AppDispatcher
import com.cjproductions.vicinity.support.user.UserSettingsDataSource
import comcjproductionsvicinitycoredatadatabase.LikeEntity
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.cancellation.CancellationException

class DefaultLikesRepository(
    private val serverlessRepository: ServerlessRepository,
    private val remoteLikesDataSource: RemoteLikesDataSource,
    private val localLikesDataSource: LocalLikesDataSource,
    private val userSettingsDataSource: UserSettingsDataSource,
    private val appDispatcher: AppDispatcher,
    private val applicationScope: CoroutineScope,
): LikesRepository, CoroutineScope {
    override val coroutineContext: CoroutineContext = SupervisorJob() + appDispatcher.io
    private val user = userSettingsDataSource.user

    override val likeCount = userSettingsDataSource.user.flatMapLatest { user ->
        if (user?.authenticated == true) {
            localLikesDataSource.getLikeCount(user.id)
        } else flowOf(0)

    }.stateIn(
        scope = this,
        started = SharingStarted.Lazily,
        initialValue = 0,
    )

    override val pagedLikes: Flow<PagingData<Like>> = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = {
            with(localLikesDataSource.queries) {
                QueryPagingSource(
                    countQuery = countLikes(user.value?.id.orEmpty()),
                    transacter = this,
                    context = appDispatcher.io,
                    queryProvider = { limit, offset ->
                        likes(
                            userId = user.value?.id.orEmpty(),
                            limit = limit,
                            offset = offset,
                        )
                    },
                )
            }
        }
    ).flow.map { pagingData: PagingData<LikeEntity> ->
        pagingData.map { likeEntity ->
            with(likeEntity) {
                Like(
                    userId = userId,
                    title = title,
                    normalizedTitle = normalizedTitle,
                    image = image,
                    startDateTime = startDateTime,
                    endDateTime = endDateTime,
                    createdAt = createdAt,
                    category = category,
                )
            }
        }
    }
    override val allLikes: Flow<List<Like>> = localLikesDataSource.likes

    private val isResumed = MutableStateFlow(true)
    private var likesJob: Job? = null

    private val coroutineExceptionHandler: CoroutineExceptionHandler =
        CoroutineExceptionHandler { _, exception ->
            exception.printStackTrace()
        }

    init {
        launch(coroutineExceptionHandler + SupervisorJob()) {
            serverlessRepository.lifecycleState.collectLatest { lifecycleState ->
                isResumed.update { lifecycleState == LifecycleState.RESUMED }
                if (lifecycleState == LifecycleState.PAUSED) {
                    likesJob?.cancel()
                    likesJob = null
                    return@collectLatest
                }
                likesJob = launch {
                    userSettingsDataSource.user.collectLatest { user ->
                        val userId = user?.id
                        if (userId == null) {
                            localLikesDataSource.deleteAllLikes()
                            return@collectLatest
                        }
                        coroutineScope {
                            launch {
                                if (!user.authenticated) return@launch
                                remoteLikesDataSource.observeLikes()
                                    .collect { remoteLikes ->
                                        if (isResumed.value) {
                                            removeOldLikes(remoteLikes)
                                            isResumed.update { false }
                                        }
                                        if (remoteLikes.isNotEmpty()) {
                                            localLikesDataSource.insertLikes(
                                                remoteLikes.toLikes()
                                            )
                                        }
                                    }
                            }
                            launch {
                                if (!user.authenticated) return@launch
                                observeDeleteChanges()
                            }
                        }
                    }
                }
            }
        }
    }

    override suspend fun isEventLiked(
        userId: String,
        title: String,
    ) = withContext(appDispatcher.io) {
        localLikesDataSource.isLiked(
            userId = userId,
            normalizedTitle = title,
        )
    }

    override suspend fun deleteAllLikes(userId: String) = withContext(appDispatcher.io) {
        remoteLikesDataSource.deleteAllLikes(userId)
        localLikesDataSource.deleteAllLikes()
    }

    override suspend fun deleteLike(like: Like): EmptyResult<DatabaseError> =
        applicationScope.async(appDispatcher.io) {
            try {
                localLikesDataSource.deleteLikes(listOf(like))
                remoteLikesDataSource.deleteLike(
                    normalizedTitle = like.normalizedTitle,
                    userId = like.userId,
                )
                Result.Success(Unit).asEmptyDataResult()
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                localLikesDataSource.insertLikes(listOf(like))
                Result.Error(DatabaseError.Unknown(e.toString())).asEmptyDataResult()
            }
        }.await()

    override suspend fun insertLike(like: Like): EmptyResult<DatabaseError> =
        applicationScope.async(appDispatcher.io) {
            try {
                localLikesDataSource.insertLikes(listOf(like))
                remoteLikesDataSource.insertLike(like.toLikeDataEntity())
                Result.Success(Unit).asEmptyDataResult()
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                Result.Error(DatabaseError.Unknown(e.toString())).asEmptyDataResult()
            }
        }.await()

    private suspend fun removeOldLikes(remoteLikes: List<LikeDataEntity>) {
        val localLikes = localLikesDataSource.likes.firstOrNull().orEmpty()
        val likeToDelete = localLikes.filter { it !in remoteLikes.toLikes() }
        localLikesDataSource.deleteLikes(likeToDelete)
    }

    private suspend fun observeDeleteChanges() {
        remoteLikesDataSource.observeLikeDeletions().collect { likeDeleteEvent ->
            localLikesDataSource.deleteLikes(
                listOf(
                    with(likeDeleteEvent) {
                        Like(
                            userId = userId,
                            normalizedTitle = normalizedTitle,
                            title = normalizedTitle,
                        )
                    }
                )
            )
        }
    }
}