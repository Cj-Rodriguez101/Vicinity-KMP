@file:OptIn(ExperimentalCoroutinesApi::class)

package com.cjproductions.vicinity.likes.domain

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow

class ObserveLikesCountUseCase(
  likesRepository: LikesRepository,
) {
  val likesCount: StateFlow<Long> = likesRepository.likeCount
}