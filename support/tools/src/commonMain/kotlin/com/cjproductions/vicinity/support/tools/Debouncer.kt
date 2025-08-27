package com.cjproductions.vicinity.support.tools

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Debouncer {
  val debounceJobs: MutableMap<String, Job?> = mutableMapOf()

  fun debounceAction(
    key: String = SEARCH_QUERY,
    scope: CoroutineScope,
    timeOut: Long = DEBOUNCE_TIMEOUT,
    action: suspend () -> Unit,
  ) {
    debounceJobs[key]?.cancel()
    debounceJobs[key] = scope.launch {
      delay(timeMillis = timeOut)
      try {
        action()
      } finally {
        debounceJobs.remove(key)
      }
    }
  }

  fun cancelAllJobs() {
    debounceJobs.values.forEach { it?.cancel() }
  }

  companion object {
    private const val SEARCH_QUERY = "search"
    private const val DEBOUNCE_TIMEOUT = 300L
  }
}