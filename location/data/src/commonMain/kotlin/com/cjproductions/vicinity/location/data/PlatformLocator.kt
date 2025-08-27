package com.cjproductions.vicinity.location.data

import com.cjproductions.vicinity.core.domain.util.Result
import com.cjproductions.vicinity.location.domain.DomainLocationTracker
import com.cjproductions.vicinity.location.domain.model.DefaultLocation
import com.cjproductions.vicinity.location.domain.model.LocationError

expect class PlatformLocator: DomainLocationTracker {
  override suspend fun getLocation(): Result<DefaultLocation, LocationError>
}