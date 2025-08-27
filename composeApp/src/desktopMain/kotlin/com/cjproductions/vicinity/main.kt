package com.cjproductions.vicinity

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.cjproductions.vicinity.app.di.initializeKoin
import com.cjproductions.vicinity.app.presentation.App
import com.cjproductions.vicinity.app.presentation.ExternalUriHandler
import java.awt.Desktop

fun main(args: Array<String>) {
  application {
    initializeKoin()
    val externalUriHandler = ExternalUriHandler

    if (System.getProperty("os.name").indexOf("Mac") > -1) {
      Desktop.getDesktop().setOpenURIHandler { uri ->
        externalUriHandler.onNewUri(uri.uri.toString())
      }
    } else {
      args.getOrNull(0)?.let { startupUri -> externalUriHandler.onNewUri(startupUri) }
    }
    Window(
      onCloseRequest = ::exitApplication,
      title = "Vicinity",
    ) {
      App()
    }
  }
}