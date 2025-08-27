package com.cjproductions.vicinity.location.data

import com.fonfon.kgeohash.GeoHash

actual fun latLongToHash(lat: Double, long: Double) = GeoHash(lat, long, 8).toString()