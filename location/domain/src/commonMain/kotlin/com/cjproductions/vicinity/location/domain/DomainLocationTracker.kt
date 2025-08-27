package com.cjproductions.vicinity.location.domain

import com.cjproductions.vicinity.core.domain.util.Result
import com.cjproductions.vicinity.location.domain.model.DefaultLocation
import com.cjproductions.vicinity.location.domain.model.LocationError

fun interface DomainLocationTracker {
  suspend fun getLocation(): Result<DefaultLocation, LocationError>
}