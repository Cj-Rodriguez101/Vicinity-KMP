package com.cjproductions.vicinity.eventDetail.presentation.eventDetail.model

import com.cjproductions.vicinity.discover.domain.model.Image
import com.cjproductions.vicinity.discover.domain.model.VLocation
import com.cjproductions.vicinity.discover.domain.model.Venue
import com.cjproductions.vicinity.location.domain.model.UserLocation
import kotlin.math.PI
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

data class VenueUi(
  val id: String,
  val name: String,
  val city: String?,
  val state: String?,
  val images: List<Image>?,
  val country: String?,
  val distance: String?,
  val location: VLocation?,
)

private fun Venue.toVenueUi(userLocation: UserLocation?): VenueUi? {
  return try {
    VenueUi(
      id = id,
      name = name,
      city = city,
      state = state,
      country = country,
      location = location,
      images = images,
      distance = null,
    )
  } catch (ex: Exception) {
    ex.printStackTrace()
    null
  }
}

fun List<Venue>.toVenueUis(userLocation: UserLocation?): List<VenueUi> {
  return this.mapNotNull { it.toVenueUi(userLocation) }
}

private fun findDistanceInMetersBetweenTwoLocations(
  lat1: Double,
  long1: Double,
  lat2: Double,
  long2: Double,
): Double { // Changed return type to Double instead of String
  val earthRadiusKm = 6371.0 // Earth's radius in kilometers

  val dLat = toRadians(lat2 - lat1)
  val dLon = toRadians(long2 - long1) // Fixed variable name
  val lat1Rad = toRadians(lat1) // Don't reassign parameters
  val lat2Rad = toRadians(lat2)

  val a = sin(dLat / 2) * sin(dLat / 2) +
      sin(dLon / 2) * sin(dLon / 2) *
      cos(lat1Rad) * cos(lat2Rad)

  val c = 2 * asin(sqrt(a))
  val distanceKm = earthRadiusKm * c

  return distanceKm * 1000 // Convert to meters
}

private fun toRadians(deg: Double): Double = deg / 180.0 * PI