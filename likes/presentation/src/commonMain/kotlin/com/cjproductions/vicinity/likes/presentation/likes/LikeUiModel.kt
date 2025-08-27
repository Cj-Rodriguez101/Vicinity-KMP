package com.cjproductions.vicinity.likes.presentation.likes

import com.cjproductions.vicinity.core.presentation.designsystem.components.UIText

sealed class LikeUiModel {
  data class Header(val title: UIText): LikeUiModel()
  data class Item(val like: LikeUI): LikeUiModel()
}