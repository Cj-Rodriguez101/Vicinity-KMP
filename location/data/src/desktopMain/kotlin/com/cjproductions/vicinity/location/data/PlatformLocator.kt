package com.cjproductions.vicinity.location.data

import Vicinity.location.data.BuildConfig.IP_BASE_URL
import com.cjproductions.vicinity.core.domain.util.Result
import com.cjproductions.vicinity.location.data.model.LocationDto
import com.cjproductions.vicinity.location.data.model.toDefaultLocation
import com.cjproductions.vicinity.location.domain.DomainLocationTracker
import com.cjproductions.vicinity.location.domain.model.DefaultLocation
import com.cjproductions.vicinity.location.domain.model.LocationError
import com.vicinity.core.data.network.get
import io.ktor.client.HttpClient

actual class PlatformLocator(
  private val httpClient: HttpClient,
): DomainLocationTracker {
  actual override suspend fun getLocation(): Result<DefaultLocation, LocationError> {
    return httpClient.get<LocationDto>(
      route = "",
      baseUrl = IP_BASE_URL,
    ).getOrNull()?.let { locationDto ->
      Result.Success(locationDto.toDefaultLocation())
    } ?: run {
      Result.Error(LocationError.Unknown)
    }
  }
}