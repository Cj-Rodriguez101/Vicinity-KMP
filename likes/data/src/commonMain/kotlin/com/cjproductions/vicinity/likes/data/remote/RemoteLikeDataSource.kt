@file:OptIn(SupabaseExperimental::class)

package com.cjproductions.vicinity.likes.data.remote

import com.cjproductions.vicinity.likes.data.remote.model.LikeDataEntity
import com.cjproductions.vicinity.likes.data.remote.model.LikeDeleteEvent
import com.cjproductions.vicinity.support.tools.AppDispatcher
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.result.PostgrestResult
import io.github.jan.supabase.realtime.RealtimeChannel
import io.github.jan.supabase.realtime.broadcastFlow
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.selectAsFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlin.coroutines.CoroutineContext

interface RemoteLikesDataSource {
    suspend fun observeLikes(): Flow<List<LikeDataEntity>>
    suspend fun observeLikeDeletions(): Flow<LikeDeleteEvent>
    suspend fun getLikes(userId: String): List<LikeDataEntity>
    suspend fun insertLike(data: LikeDataEntity): PostgrestResult
    suspend fun deleteLike(userId: String, normalizedTitle: String): PostgrestResult
    suspend fun deleteAllLikes(userId: String)
}

class DefaultRemoteLikesDataSource(
    private val supabaseClient: SupabaseClient,
    appDispatcher: AppDispatcher,
): RemoteLikesDataSource, CoroutineScope {

    override val coroutineContext: CoroutineContext = SupervisorJob() + appDispatcher.io
    private var deleteChannel: RealtimeChannel? = null

    override suspend fun observeLikes() =
        supabaseClient.from(LIKE_DATABASE_KEY).selectAsFlow(
            primaryKey = LikeDataEntity::userId,
            channelName = null,
        )

    /**
     * Observes real-time delete events for user likes.
     *
     * Required because `selectAsFlow` returns empty results on deletions.
     *
     * @param userId The user ID to observe like deletions for
     * @return Flow of [LikeDeleteEvent] when likes are deleted
     */
    override suspend fun observeLikeDeletions(): Flow<LikeDeleteEvent> {
        return channelFlow {
            deleteChannel?.unsubscribe()
            deleteChannel = supabaseClient.channel(LIKE_DELETE_CHANNEL)

            val deleteFlow = deleteChannel?.broadcastFlow<LikeDeleteEvent>(event = DELETE_EVENT)

            try {
                deleteChannel?.subscribe()
                deleteFlow?.collect { send(it) }
            } finally {
                deleteChannel?.unsubscribe()
                if (deleteChannel != null) {
                    deleteChannel = null
                }
            }
        }
//        val channel = supabaseClient.channel(LIKE_DELETE_CHANNEL)
//
//        return flow {
//            val deleteFlow = channel.broadcastFlow<LikeDeleteEvent>(event = DELETE_EVENT)
//            channel.subscribe()
//            emitAll(deleteFlow)
//        }.onCompletion {
//            channel.unsubscribe()
//        }
    }

    override suspend fun getLikes(
        userId: String,
    ): List<LikeDataEntity> {
        return supabaseClient.from(LIKE_DATABASE_KEY).select {
            filter {
                eq(
                    column = USER_ID,
                    value = userId,
                )
            }
        }.decodeList()
    }

    override suspend fun insertLike(
        data: LikeDataEntity,
    ): PostgrestResult = supabaseClient.from(LIKE_DATABASE_KEY).insert(value = data)

    override suspend fun deleteLike(
        userId: String,
        normalizedTitle: String,
    ): PostgrestResult {
        return supabaseClient.from(LIKE_DATABASE_KEY).delete {
            filter {
                eq(
                    column = USER_ID,
                    value = userId,
                )
                eq(
                    column = NORMALIZED_TITLE,
                    value = normalizedTitle,
                )
            }
        }.also {
            println("deleteLike: after $it")
            supabaseClient.channel(LIKE_DELETE_CHANNEL).broadcast(
                event = DELETE_EVENT,
                message = buildJsonObject {
                    put(USER_ID, userId)
                    put(NORMALIZED_TITLE, normalizedTitle)
                }
            )
            println("deleteLike: after broadcast")
        }
    }

    override suspend fun deleteAllLikes(userId: String) {
        supabaseClient.from(LIKE_DATABASE_KEY).delete {
            filter {
                eq(
                    column = USER_ID,
                    value = userId,
                )
            }
        }
    }

    companion object {
        private const val USER_ID = "user_id"
        private const val NORMALIZED_TITLE = "normalized_title"
        private const val DELETE_EVENT = "DELETE"
        private const val LIKE_DATABASE_KEY = "likes"
        private const val LIKE_DELETE_CHANNEL = "likes_delete"
    }
}