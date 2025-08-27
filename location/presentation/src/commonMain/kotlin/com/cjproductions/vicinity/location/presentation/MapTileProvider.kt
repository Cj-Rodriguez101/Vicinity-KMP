package com.cjproductions.vicinity.location.presentation

import kotlinx.io.RawSource
import ovh.plrapps.mapcompose.core.TileStreamProvider

expect class MapTileProvider: TileStreamProvider {
  override suspend fun getTileStream(
    row: Int,
    col: Int,
    zoomLvl: Int,
  ): RawSource?
}