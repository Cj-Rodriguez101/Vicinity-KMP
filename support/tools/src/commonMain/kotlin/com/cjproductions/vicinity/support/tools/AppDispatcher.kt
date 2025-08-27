package com.cjproductions.vicinity.support.tools

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

interface AppDispatcher {
  val io: CoroutineDispatcher
  val main: CoroutineDispatcher
  val default: CoroutineDispatcher
}

class DefaultAppDispatcher: AppDispatcher {
  override val io: CoroutineDispatcher = Dispatchers.IO
  override val main: CoroutineDispatcher = Dispatchers.Main
  override val default: CoroutineDispatcher = Dispatchers.Default
}