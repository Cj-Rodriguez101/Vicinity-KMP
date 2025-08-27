package com.cjproductions.vicinity.app.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cjproductions.vicinity.app.presentation.MainViewAction.OnPauseRealTimeConnection
import com.cjproductions.vicinity.app.presentation.MainViewAction.OnResumeRealTimeConnection
import com.cjproductions.vicinity.auth.domain.ObserveLoggedInStateUseCase
import com.cjproductions.vicinity.core.domain.serverless.ServerlessRepository
import com.cjproductions.vicinity.core.presentation.ui.SnackBarController
import com.vicinity.core.data.connectivity.ConnectivityRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(
  private val serverlessRepository: ServerlessRepository,
  snackBarController: SnackBarController,
  connectivityRepository: ConnectivityRepository,
  observeAuthStateUseCase: ObserveLoggedInStateUseCase,
): ViewModel() {

  val events = snackBarController.events

  val isLoggedIn: StateFlow<Boolean> = observeAuthStateUseCase.isLoggedIn

  val networkStatusUpdates = connectivityRepository.statusUpdates

  fun onAction(action: MainViewAction) {
    when (action) {
      OnPauseRealTimeConnection -> viewModelScope.launch { serverlessRepository.pauseRealtimeConnection() }
      OnResumeRealTimeConnection -> viewModelScope.launch { serverlessRepository.resumeRealtimeConnection() }
    }
  }
}