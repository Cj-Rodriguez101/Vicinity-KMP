package com.cjproductions.vicinity.support.tools

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

/**
 * [CoroutineScope] tied to the application lifecycle.
 *
 * This scope is suitable for launching coroutines that should complete
 * their work regardless of whether a specific screen or component is
 * visible or dismissed. It uses a [SupervisorJob] to ensure that
 * a failure in one child coroutine does not cancel other children.
 *
 * Operations launched within this scope will continue to run as long as
 * the application process is alive.
 */
data object ApplicationScope {
  val scope = CoroutineScope(SupervisorJob())
}