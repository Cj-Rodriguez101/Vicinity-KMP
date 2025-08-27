package com.cjproductions.vicinity.geoEvents.presentation.globalEvents

import com.cjproductions.vicinity.location.presentation.CoordinateConverter
import ovh.plrapps.mapcompose.api.BoundingBox

internal fun convertBoundingBoxToMapBounds(boundingBox: BoundingBox): MapBounds {
  val southWest = CoordinateConverter.normalizedToLatLng(
    x = boundingBox.xLeft,
    y = boundingBox.yBottom
  )
  val northEast = CoordinateConverter.normalizedToLatLng(
    x = boundingBox.xRight,
    y = boundingBox.yTop
  )

  return MapBounds(
    northEast = LatLng(northEast.first, northEast.second),
    southWest = LatLng(southWest.first, southWest.second)
  )
}