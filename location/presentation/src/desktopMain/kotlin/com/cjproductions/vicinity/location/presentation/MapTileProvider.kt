package com.cjproductions.vicinity.location.presentation

import kotlinx.io.RawSource
import kotlinx.io.asSource
import ovh.plrapps.mapcompose.core.TileStreamProvider
import java.net.HttpURLConnection
import java.net.URL

actual class MapTileProvider(): TileStreamProvider {
  actual override suspend fun getTileStream(
    row: Int,
    col: Int,
    zoomLvl: Int,
  ): RawSource? {
    return try {
      val url = URL("https://tile.openstreetmap.org/$zoomLvl/$col/$row.png")
      val connection = url.openConnection() as HttpURLConnection
      connection.setRequestProperty("User-Agent", "Chrome/120.0.0.0 Safari/537.36")
      connection.doInput = true
      connection.connect()
      connection.inputStream.asSource()
    } catch (e: Exception) {
      e.printStackTrace()
      null
    }
  }
}