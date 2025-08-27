package com.cjproductions.vicinity.core.domain.util

sealed interface Result<out D, out E: Error> {
  data class Success<out D>(val data: D): Result<D, Nothing>
  data class Error<out E: com.cjproductions.vicinity.core.domain.util.Error>(val error: E):
    Result<Nothing, E>

  val isSuccess: Boolean
    get() = this is Success

  val isFailure: Boolean
    get() = this is Error

  fun getOrNull(): D? = when (this) {
    is Success -> data
    is Error -> null
  }
}

inline fun <D, E: Error> Result<D, E>.onSuccess(action: (D) -> Unit): Result<D, E> {
  if (this is Result.Success) {
    action(data)
  }
  return this
}

inline fun <D, E: Error> Result<D, E>.onFailure(action: (E) -> Unit): Result<D, E> {
  if (this is Result.Error) {
    action(error)
  }
  return this
}

inline fun <T, E: Error, R> Result<T, E>.map(map: (T) -> R): Result<R, E> {
  return when (this) {
    is Result.Error -> Result.Error(error)
    is Result.Success -> Result.Success(map(data))
  }
}

fun <T, E: Error> Result<T, E>.asEmptyDataResult(): EmptyResult<E> {
  return map { }
}

typealias EmptyResult<E> = Result<Unit, E>