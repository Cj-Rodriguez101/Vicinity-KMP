package com.cjproductions.vicinity.likes.domain

import androidx.paging.PagingData
import com.cjproductions.vicinity.core.domain.util.EmptyResult
import com.cjproductions.vicinity.core.domain.util.Error
import com.cjproductions.vicinity.likes.domain.model.Like
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface LikesRepository {
  val likeCount: StateFlow<Long>
  val pagedLikes: Flow<PagingData<Like>>
  val allLikes: Flow<List<Like>>
  suspend fun isEventLiked(
    userId: String,
    title: String,
  ): Boolean

  suspend fun deleteAllLikes(userId: String)
  suspend fun deleteLike(like: Like): EmptyResult<DatabaseError>
  suspend fun insertLike(like: Like): EmptyResult<DatabaseError>
}

sealed class DatabaseError: Error {
  data class Unknown(val message: String): DatabaseError()
}