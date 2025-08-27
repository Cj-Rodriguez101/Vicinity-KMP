package com.cjproductions.vicinity.app.presentation

sealed interface MainViewAction {
  data object OnPauseRealTimeConnection: MainViewAction
  data object OnResumeRealTimeConnection: MainViewAction
}