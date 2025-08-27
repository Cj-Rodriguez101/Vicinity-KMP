@file:OptIn(ExperimentalCoroutinesApi::class)

package com.cjproductions.vicinity.likes.presentation.likes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import com.cjproductions.vicinity.auth.domain.ObserveLoggedInStateUseCase
import com.cjproductions.vicinity.core.domain.util.onSuccess
import com.cjproductions.vicinity.core.presentation.designsystem.components.UIText.StringResourceId
import com.cjproductions.vicinity.core.presentation.ui.SnackBarController
import com.cjproductions.vicinity.core.presentation.ui.SnackBarEvent.SnackBarUiEvent
import com.cjproductions.vicinity.likes.domain.LikesRepository
import com.cjproductions.vicinity.likes.domain.ToggleLikeUseCase
import com.cjproductions.vicinity.likes.presentation.likes.LikeAction.OnLikeClick
import com.cjproductions.vicinity.likes.presentation.likes.LikeAction.OnShareClick
import com.cjproductions.vicinity.likes.presentation.likes.LikeUiModel.Header
import com.cjproductions.vicinity.likes.presentation.likes.LikeUiModel.Item
import com.cjproductions.vicinity.support.haptics.presentation.PerformClickHapticsUseCase
import com.cjproductions.vicinity.support.share.presentation.ShareLinkUseCase
import com.cjproductions.vicinity.support.tools.getCurrentTime
import com.cjproductions.vicinity.support.tools.toDouble
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import vicinity.core.presentation.ui.generated.resources.removed_from_liked_events
import vicinity.likes.presentation.generated.resources.Res
import vicinity.likes.presentation.generated.resources.past
import vicinity.likes.presentation.generated.resources.upcoming

class LikesViewModel(
  private val toggleLike: ToggleLikeUseCase,
  private val shareLinkUseCase: ShareLinkUseCase,
  private val performClickHaptics: PerformClickHapticsUseCase,
  private val snackBarController: SnackBarController,
  likesRepository: LikesRepository,
  observeLoggedInState: ObserveLoggedInStateUseCase,
): ViewModel() {

  private val pagedLikes = likesRepository.pagedLikes.cachedIn(viewModelScope)

  val likes: Flow<PagingData<LikeUiModel>> = pagedLikes
    .map { pagingData ->
      pagingData
        .map { like -> Item(like.toLikeUI()) }
        .insertSeparators { before, after ->
          if (shouldShowUpcoming(before, after)) {
            Header(StringResourceId(Res.string.upcoming))
          } else if (shouldShowPast(before, after)) {
            Header(StringResourceId(Res.string.past))
          } else null
        }
    }.cachedIn(viewModelScope)

  private val currentTime = getCurrentTime()

  private fun shouldShowPast(
    before: Item?,
    after: Item?,
  ): Boolean = (before?.like?.startDateTime?.toDouble()
    ?: Int.MAX_VALUE.toDouble()) > currentTime && after != null &&
      (after.like.startDateTime?.toDouble()
        ?: Int.MIN_VALUE.toDouble()) < currentTime

  private fun shouldShowUpcoming(
    before: Item?,
    after: Item?,
  ) = before == null && after != null && (after.like.startDateTime?.toDouble()
    ?: Int.MAX_VALUE.toDouble()) > currentTime

  val isLoggedIn = observeLoggedInState.isLoggedIn

  fun onAction(action: LikeAction) {
    when (action) {
      is OnLikeClick -> unlike(action.like)
      is OnShareClick -> viewModelScope.launch { shareLinkUseCase.invoke(action.title) }
    }
  }

  private fun unlike(like: LikeUI) {
    viewModelScope.launch {
      performClickHaptics.invoke()
      with(like) {
        toggleLike.invoke(
          normalizedTitle = normalizedTitle,
          title = title,
          category = category,
          isLiked = true,
        ).onSuccess {
          snackBarController.sendEvent(
            SnackBarUiEvent(
              StringResourceId(vicinity.core.presentation.ui.generated.resources.Res.string.removed_from_liked_events)
            )
          )
        }
      }
    }
  }
}