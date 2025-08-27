package com.cjproductions.vicinity.location.presentation

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.prepareGet
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.readBuffer
import kotlinx.io.RawSource
import ovh.plrapps.mapcompose.core.TileStreamProvider

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class MapTileProvider(val httpClient: HttpClient): TileStreamProvider {
  actual override suspend fun getTileStream(
    row: Int,
    col: Int,
    zoomLvl: Int,
  ): RawSource? {
    return try {
      //val url = URL("https://tile.openstreetmap.org/$zoomLvl/$col/$row.png")
      httpClient.readBuffer(
        path = "https://tile.openstreetmap.org/$zoomLvl/$col/$row.png"
      )
    } catch (e: Exception) {
      e.printStackTrace()
      null
    }
  }
}

private suspend fun HttpClient.readBuffer(path: String): RawSource {
  return this.prepareGet(path).execute { httpResponse ->
    val channel: ByteReadChannel = httpResponse.body()
    channel.readBuffer()
  }
}