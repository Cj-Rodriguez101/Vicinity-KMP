@file:OptIn(ExperimentalCoroutinesApi::class, ExperimentalSettingsApi::class)

package com.cjproductions.vicinity.support.user

import com.cjproductions.vicinity.support.tools.AppDispatcher
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.coroutines.getStringOrNullStateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlin.coroutines.CoroutineContext

interface UserSettingsDataSource {
  val user: StateFlow<User?>
  suspend fun updateUser(user: User?)
}

class DefaultUserSettingsDataSource(
  private val settings: ObservableSettings,
  private val applicationScope: CoroutineScope,
  appDispatcher: AppDispatcher,
): UserSettingsDataSource, CoroutineScope {
  override val coroutineContext: CoroutineContext = SupervisorJob() + appDispatcher.io
  override val user: StateFlow<User?> = settings.getStringOrNullStateFlow(
    coroutineScope = this,
    key = USER_KEY
  ).flatMapLatest { user ->
    flow {
      emit(
        if (user.isNullOrEmpty()) null
        else Json.decodeFromString(User.serializer(), user)
      )
    }
  }.catch { emit(null) }.stateIn(
    scope = this,
    started = kotlinx.coroutines.flow.SharingStarted.Eagerly,
    initialValue = null
  )

  override suspend fun updateUser(user: User?) {
    applicationScope.launch {
      settings.putString(
        USER_KEY,
        if (user == null) "" else Json.encodeToString(User.serializer(), user)
      )
    }.join()
  }

  companion object {
    private const val USER_KEY = "user"
  }
}