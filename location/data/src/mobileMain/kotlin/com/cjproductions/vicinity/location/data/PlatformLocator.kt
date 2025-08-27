package com.cjproductions.vicinity.location.data

import com.cjproductions.vicinity.core.domain.util.Result
import com.cjproductions.vicinity.location.domain.DomainLocationTracker
import com.cjproductions.vicinity.location.domain.model.DefaultLocation
import com.cjproductions.vicinity.location.domain.model.LocationError
import com.cjproductions.vicinity.support.tools.Platform
import com.cjproductions.vicinity.support.tools.PlatformType
import dev.jordond.compass.Priority
import dev.jordond.compass.geocoder.Geocoder
import dev.jordond.compass.geocoder.exception.GeocodeException
import dev.jordond.compass.geolocation.Geolocator
import dev.jordond.compass.geolocation.GeolocatorResult
import dev.jordond.compass.geolocation.GeolocatorResult.NotFound
import dev.jordond.compass.geolocation.isPermissionDeniedForever
import kotlinx.coroutines.CancellationException

actual class PlatformLocator(
  private val geoLocator: Geolocator,
  private val geocoder: Geocoder,
): DomainLocationTracker {
  actual override suspend fun getLocation(): Result<DefaultLocation, LocationError> {
    return try {
      val geoLocatorResult = geoLocator.current(Priority.HighAccuracy)
      if (geoLocatorResult is GeolocatorResult.Success) {
        val location = geoLocatorResult.getOrNull()?.let {
          val place = geocoder.platformGeocoder.reverse(
            latitude = it.coordinates.latitude,
            longitude = it.coordinates.longitude,
          ).firstOrNull()
          DefaultLocation(
            latitude = it.coordinates.latitude,
            longitude = it.coordinates.longitude,
            city = place?.administrativeArea.takeUnless { Platform.type == PlatformType.IOS }
              ?: place?.locality,
            country = place?.country,
            countryCode = place?.isoCountryCode?.lowercase().orEmpty(),
          )
        } ?: return Result.Error(LocationError.Unknown)
        return Result.Success(location)
      } else {
        (geoLocatorResult as GeolocatorResult.Error).let { error ->
          when (error.errorOrNull()) {
            NotFound -> Result.Error(LocationError.NotFound)
            GeolocatorResult.NotSupported -> Result.Error(LocationError.NotSupported)
            is GeolocatorResult.PermissionDenied -> {
              Result.Error(LocationError.PermissionDenied(error.isPermissionDeniedForever()))
            }

            is GeolocatorResult.GeolocationFailed -> {
              Result.Error(LocationError.GeolocationFailed(error.message))
            }

            else -> Result.Error(LocationError.Unknown)
          }
        }
      }
    } catch (e: GeocodeException) {
      if (e is CancellationException) throw e
      Result.Error(LocationError.Unknown)
    }
  }
}