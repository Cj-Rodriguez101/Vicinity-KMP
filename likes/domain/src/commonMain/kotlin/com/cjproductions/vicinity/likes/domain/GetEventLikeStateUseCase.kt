@file:OptIn(ExperimentalCoroutinesApi::class)

package com.cjproductions.vicinity.likes.domain

import com.cjproductions.vicinity.auth.domain.ObserveLoggedInStateUseCase
import com.cjproductions.vicinity.support.tools.AppDispatcher
import com.cjproductions.vicinity.support.user.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.firstOrNull
import kotlin.coroutines.CoroutineContext

class GetEventLikeStateUseCase(
  private val likesRepository: LikesRepository,
  private val observeLoggedInStateUseCase: ObserveLoggedInStateUseCase,
  private val userRepository: UserRepository,
  appDispatcher: AppDispatcher,
): CoroutineScope {

  override val coroutineContext: CoroutineContext = SupervisorJob() + appDispatcher.main

  suspend fun invoke(title: String): LikeState {
    if (!observeLoggedInStateUseCase.isLoggedIn.value) return LikeState.Hide
    val isLiked = likesRepository.isEventLiked(
      userId = userRepository.user.firstOrNull()?.id.orEmpty(),
      title = title,
    )
    return LikeState.Show(isLiked)
  }
}