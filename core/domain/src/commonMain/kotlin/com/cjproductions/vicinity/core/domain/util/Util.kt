package com.cjproductions.vicinity.core.domain.util

import kotlin.coroutines.cancellation.CancellationException

/**
 * Executes a suspending [block], catching exceptions except for [CancellationException].
 *
 * @param T The return type of the [block].
 * @param block The suspending function to execute.
 * @param defaultErrorResult The default result if an exception occurs.
 * @param onError Optional lambda for custom error handling; if null, [defaultErrorResult] is used.
 * @return The result of [block], the result of [onError] if it is not null and
 * an exception occurs or [defaultErrorResult] if [onError] is null and an exception occurs.
 * @throws CancellationException if the coroutine is cancelled.
 */

suspend fun <T> executeWithCancellationPassthrough(
  block: suspend () -> T,
  defaultErrorResult: T? = null,
  onError: ((Exception) -> T)? = null,
): T? {
  return try {
    block()
  } catch (e: Exception) {
    if (e is CancellationException) throw e
    println(e.toString())
    onError?.invoke(e) ?: defaultErrorResult
  }
}