package com.cjproductions.vicinity.app.presentation

sealed class MainViewState {
  data object Loading: MainViewState()
  data class Loaded(val isLoggedIn: Boolean): MainViewState()
}