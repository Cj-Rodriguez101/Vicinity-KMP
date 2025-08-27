package com.cjproductions.vicinity.likes.domain

sealed class LikeState {
  data class Show(val liked: Boolean = false): LikeState()
  data object Hide: LikeState()
}

fun LikeState.isLiked(): Boolean {
  return when (this) {
    is LikeState.Show -> liked
    LikeState.Hide -> false
  }
}