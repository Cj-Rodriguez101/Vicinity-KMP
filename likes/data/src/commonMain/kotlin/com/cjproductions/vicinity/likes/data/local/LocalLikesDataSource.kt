package com.cjproductions.vicinity.likes.data.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import com.cjproductions.vicinity.core.data.database.VicinityDatabase
import com.cjproductions.vicinity.likes.data.local.model.toLikes
import com.cjproductions.vicinity.likes.domain.model.Like
import com.cjproductions.vicinity.support.tools.AppDispatcher
import comcjproductionsvicinitycoredatadatabase.VicinityDbQueries
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface LocalLikesDataSource {
  suspend fun getLikeCount(userId: String): Flow<Long>
  val likes: Flow<List<Like>>
  val queries: VicinityDbQueries
  suspend fun deleteAllLikes()
  suspend fun insertLikes(likes: List<Like>)
  suspend fun deleteLikes(likes: List<Like>)
  suspend fun isLiked(
    userId: String,
    normalizedTitle: String,
  ): Boolean
}

class DefaultLocalLikesDataSource(
  private val database: VicinityDatabase,
  private val appDispatcher: AppDispatcher,
): LocalLikesDataSource {

  override val queries = database.vicinityDbQueries
  override suspend fun getLikeCount(userId: String) =
    database.vicinityDbQueries.countLikes(userId).asFlow().mapToOne(appDispatcher.io)

  override val likes = database.vicinityDbQueries.getAllLikes().asFlow()
    .mapToList(appDispatcher.io)
    .map { it.toLikes() }

  override suspend fun deleteAllLikes() {
    database.vicinityDbQueries.deleteAllLikes()
  }

  override suspend fun insertLikes(likes: List<Like>) {
    if (likes.isEmpty()) return
    database.transaction {
      likes.forEach { like ->
        with(like) {
          database.vicinityDbQueries.insertLike(
            userId = userId,
            normalizedTitle = normalizedTitle,
            title = title,
            category = category,
            image = image,
            startDateTime = startDateTime,
            endDateTime = endDateTime,
            createdAt = createdAt ?: 0.0,
          )
        }
      }
    }
  }

  override suspend fun deleteLikes(likes: List<Like>) {
    if (likes.isEmpty()) return
    database.transaction {
      likes.forEach { like ->
        with(like) {
          database.vicinityDbQueries.deleteSpecificLike(
            userId = userId,
            normalizedTitle = normalizedTitle
          )
        }
      }
    }
  }

  override suspend fun isLiked(userId: String, normalizedTitle: String): Boolean {
    return database.vicinityDbQueries.isLiked(
      userId = userId,
      title = normalizedTitle
    ).executeAsOneOrNull() == true
  }
}