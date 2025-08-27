package com.cjproductions.vicinity.location.data

import ch.hsr.geohash.GeoHash

actual fun latLongToHash(lat: Double, long: Double): String {
  return GeoHash.withCharacterPrecision(
    /* latitude = */ lat,
    /* longitude = */ long,
    /* numberOfCharacters = */ 8,
  ).toBase32()
}