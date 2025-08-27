package com.cjproductions.vicinity.location.presentation

import java.awt.Desktop
import java.net.URI

actual class MapController() {
  actual fun openMapCoordinates(lat: Double, long: Double) {
    if (lat !in -90.0..90.0 || long !in -180.0..180.0) return

    val url = "https://www.google.com/maps?q=$lat,$long"

    try {
      if (Desktop.isDesktopSupported() && Desktop.getDesktop()
          .isSupported(Desktop.Action.BROWSE)
      ) {
        Desktop.getDesktop().browse(URI(url))
      } else {
        val os = System.getProperty("os.name").lowercase()
        val command = when {
          os.contains("win") -> "rundll32 url.dll,FileProtocolHandler $url"
          os.contains("mac") -> "open $url"
          else -> "xdg-open $url"
        }
        Runtime.getRuntime().exec(command)
      }
    } catch (e: Exception) {
      println("Could not open map. Please visit: $url")
    }
  }
}