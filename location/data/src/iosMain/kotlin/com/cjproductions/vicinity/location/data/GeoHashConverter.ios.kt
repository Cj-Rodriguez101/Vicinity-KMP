package com.cjproductions.vicinity.location.data

actual fun latLongToHash(
  lat: Double,
  long: Double,
): String {
  return GeoHashConverter.encode(lat, long)
}

/**
 * Geohash implementation for Kotlin Multiplatform
 * Based on the algorithm used in nh7a/Geohash Swift library
 */
object GeoHashConverter {

  private const val BASE32 = "0123456789bcdefghjkmnpqrstuvwxyz"

  /**
   * Encodes latitude and longitude to geohash string
   *
   * @param latitude Latitude coordinate
   * @param longitude Longitude coordinate
   * @param length Length of the resulting geohash (default: 12)
   * @return Geohash string
   */
  fun encode(latitude: Double, longitude: Double, length: Int = 8): String {
    require(latitude in -90.0..90.0) { "Latitude must be between -90 and 90" }
    require(longitude in -180.0..180.0) { "Longitude must be between -180 and 180" }
    require(length > 0) { "Length must be positive" }

    var latMin = -90.0
    var latMax = 90.0
    var lonMin = -180.0
    var lonMax = 180.0

    var isEven = true
    var bit = 0
    var idx = 0
    val geohash = StringBuilder()

    while (geohash.length < length) {
      if (isEven) {
        val mid = (lonMin + lonMax) / 2
        if (longitude >= mid) {
          idx = (idx shl 1) or 1
          lonMin = mid
        } else {
          idx = idx shl 1
          lonMax = mid
        }
      } else {
        val mid = (latMin + latMax) / 2
        if (latitude >= mid) {
          idx = (idx shl 1) or 1
          latMin = mid
        } else {
          idx = idx shl 1
          latMax = mid
        }
      }

      isEven = !isEven
      bit++

      if (bit == 5) {
        geohash.append(BASE32[idx])
        bit = 0
        idx = 0
      }
    }

    return geohash.toString()
  }
}