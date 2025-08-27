package com.cjproductions.vicinity.support.user

import kotlinx.coroutines.flow.Flow

interface UserRepository {
  val user: Flow<User?>
  suspend fun updateUser(user: User?)
}

class DefaultUserRepository(
  private val userSettingsDataSource: UserSettingsDataSource,
): UserRepository {
  override val user: Flow<User?> = userSettingsDataSource.user

  override suspend fun updateUser(user: User?) {
    userSettingsDataSource.updateUser(user)
  }
}