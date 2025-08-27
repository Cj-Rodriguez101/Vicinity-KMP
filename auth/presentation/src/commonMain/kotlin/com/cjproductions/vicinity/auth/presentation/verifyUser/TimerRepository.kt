package com.cjproductions.vicinity.auth.presentation.verifyUser

import com.cjproductions.vicinity.auth.presentation.verifyUser.TimerState.Finished
import com.cjproductions.vicinity.auth.presentation.verifyUser.TimerState.Running
import com.cjproductions.vicinity.support.tools.AppDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

interface TimerRepository {
  val timer: Flow<TimerState>
  fun startTimer()
}

class DefaultTimerRepository(
  private val appDispatcher: AppDispatcher,
): TimerRepository, CoroutineScope {
  override val coroutineContext: CoroutineContext = SupervisorJob() + appDispatcher.default
  private var timerJob: Job? = null
  private val startDuration = START_DURATION.toDuration(DurationUnit.MINUTES)
  private var currentDuration = startDuration

  private val _timer: MutableStateFlow<TimerState> =
    MutableStateFlow(Running(startDuration.toMinutesSecondsString()))
  override val timer = _timer.asStateFlow()

  init {
    startTimer()
  }

  override fun startTimer() {
    timerJob?.cancel()
    timerJob = null
    currentDuration = startDuration
    timerJob = launch(appDispatcher.default) {
      while (currentDuration > Duration.ZERO) {
        delay(INCREMENT_TIME)
        currentDuration = currentDuration.minus(1.toDuration(DurationUnit.SECONDS))
        _timer.update { Running(currentDuration.toMinutesSecondsString()) }
      }
      _timer.update { Finished }
    }
  }

  private fun Duration.toMinutesSecondsString(): String =
    this.toComponents { minutes, seconds, _ ->
      "${minutes.toString().getPaddedTime()} $SEPARATOR ${seconds.toString().getPaddedTime()}"
    }

  private fun String.getPaddedTime(): String = this.padStart(
    length = TIME_PADDING,
    padChar = PAD_CHAR,
  )

  companion object {
    private const val INCREMENT_TIME = 1000L
    private const val START_DURATION = 5
    private const val TIME_PADDING = 2
    private const val PAD_CHAR = '0'
    private const val SEPARATOR = ":"
  }
}

sealed class TimerState {
  data class Running(val timeElapsed: String): TimerState()
  data object Finished: TimerState()
}