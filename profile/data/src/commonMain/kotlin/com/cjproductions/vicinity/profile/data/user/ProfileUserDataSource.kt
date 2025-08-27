@file:OptIn(SupabaseExperimental::class)

package com.cjproductions.vicinity.profile.data.user

import com.cjproductions.vicinity.profile.data.user.model.UserDataEntity
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.result.PostgrestResult
import io.github.jan.supabase.realtime.PrimaryKey
import io.github.jan.supabase.realtime.selectAsFlow
import io.github.jan.supabase.realtime.selectSingleValueAsFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.retry

interface ProfileUserDataSource {
  suspend fun updateUser(user: UserDataEntity): UserDataEntity
  suspend fun upsertUser(user: UserDataEntity): UserDataEntity
  suspend fun deleteUser(userId: String): PostgrestResult
  suspend fun observeUserDataById(userId: String): Flow<UserDataEntity?>
  suspend fun getUserDataById(userId: String): UserDataEntity?
  suspend fun getAllUsers(): Flow<List<UserDataEntity>>
}

class DefaultProfileUserDataSource(
  private val supabaseClient: SupabaseClient,
): ProfileUserDataSource {
  override suspend fun updateUser(user: UserDataEntity): UserDataEntity {
    return supabaseClient.from(USER_DATABASE_KEY).update(
      {
        UserDataEntity::name setTo user.name
        UserDataEntity::email setTo user.email
        user.image?.let { UserDataEntity::image setTo it }
        user.bio?.let { UserDataEntity::bio setTo it }
        user.interests?.let { UserDataEntity::interests setTo it }
      },
    ) {
      select()
      filter { eq(USER_ID, user.userId) }
    }.decodeSingle<UserDataEntity>()
  }

  override suspend fun upsertUser(user: UserDataEntity): UserDataEntity {
    return supabaseClient.from(USER_DATABASE_KEY).upsert(user) {
      select()
      filter { eq(USER_ID, user.userId) }
    }.decodeSingle<UserDataEntity>()
  }

  override suspend fun deleteUser(userId: String): PostgrestResult {
    println("delete Profile delete user before $userId")
//        supabaseClient.auth.admin.deleteUser(userId)
//        supabaseClient.postgrest.rpc("delete_user", mapOf("user_id" to userId))
    println("delete Profile delete user after $userId")
    return supabaseClient.from(USER_DATABASE_KEY).delete { filter { eq(USER_ID, userId) } }
  }

  override suspend fun observeUserDataById(userId: String): Flow<UserDataEntity?> =
    supabaseClient.from(USER_DATABASE_KEY).selectSingleValueAsFlow(
      primaryKey = PrimaryKey(USER_ID) { it.userId },
      channelName = null,
      filter = { eq(USER_ID, userId) }
    )

  override suspend fun getUserDataById(userId: String): UserDataEntity? {
    return supabaseClient.from(USER_DATABASE_KEY).select {
      filter { eq(USER_ID, userId) }
    }.decodeSingleOrNull<UserDataEntity>()
  }

  override suspend fun getAllUsers(): Flow<List<UserDataEntity>> {
    return supabaseClient.from(USER_DATABASE_KEY).selectAsFlow(UserDataEntity::userId)
      .retry(retries = 3).distinctUntilChanged()
  }

  companion object {
    private const val USER_DATABASE_KEY = "profiles"
    private const val USER_ID = "user_id"
  }
}