package com.cjproductions.vicinity.location.domain.model

import com.cjproductions.vicinity.core.domain.util.Error

sealed class LocationError: Error {
  data object NotFound: LocationError()
  data object NotSupported: LocationError()
  data class PermissionDenied(val forever: Boolean): LocationError()
  data class GeolocationFailed(val message: String): LocationError()
  data object Unknown: LocationError()
}