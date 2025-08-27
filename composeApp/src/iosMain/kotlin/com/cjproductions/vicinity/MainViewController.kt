package com.cjproductions.vicinity

import androidx.compose.ui.window.ComposeUIViewController
import com.cjproductions.vicinity.app.di.initializeKoin
import com.cjproductions.vicinity.app.presentation.App

fun MainViewController() = ComposeUIViewController(
  configure = { initializeKoin() }
) { App() }