package com.cjproductions.vicinity.likes.presentation.likes

import com.cjproductions.vicinity.core.domain.util.onSuccess
import com.cjproductions.vicinity.core.presentation.designsystem.components.UIText
import com.cjproductions.vicinity.core.presentation.ui.SnackBarController
import com.cjproductions.vicinity.core.presentation.ui.SnackBarEvent.SnackBarUiEvent
import com.cjproductions.vicinity.likes.domain.ToggleLikeUseCase
import com.cjproductions.vicinity.support.haptics.presentation.PerformClickHapticsUseCase
import vicinity.core.presentation.ui.generated.resources.Res
import vicinity.core.presentation.ui.generated.resources.added_to_liked_events
import vicinity.core.presentation.ui.generated.resources.removed_from_liked_events

class ToggleLikePerformHapticsUseCase(
  private val toggleLike: ToggleLikeUseCase,
  private val performClickHaptics: PerformClickHapticsUseCase,
  private val snackBarController: SnackBarController,
) {
  suspend fun invoke(
    normalizedTitle: String,
    title: String,
    category: String,
    isLiked: Boolean,
    image: String? = null,
  ) {
    toggleLike.invoke(
      normalizedTitle = normalizedTitle,
      title = title,
      category = category,
      isLiked = isLiked,
      image = image,
    ).onSuccess {
      snackBarController.sendEvent(
        SnackBarUiEvent(
          UIText.StringResourceId(Res.string.added_to_liked_events)
            .takeUnless { isLiked }
            ?: UIText.StringResourceId(Res.string.removed_from_liked_events)
        )
      )
    }
    performClickHaptics.invoke()
  }
}