package com.cjproductions.vicinity.likes.presentation.likes

sealed interface LikeAction {
  data class OnLikeClick(val like: LikeUI): LikeAction
  data class OnShareClick(val title: String): LikeAction
}