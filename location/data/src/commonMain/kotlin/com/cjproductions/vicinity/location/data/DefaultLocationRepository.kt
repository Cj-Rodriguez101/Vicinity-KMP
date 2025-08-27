@file:OptIn(ExperimentalSettingsApi::class, ExperimentalSerializationApi::class)

package com.cjproductions.vicinity.location.data

import com.cjproductions.vicinity.core.domain.util.Result
import com.cjproductions.vicinity.core.domain.util.executeWithCancellationPassthrough
import com.cjproductions.vicinity.location.domain.LocationRepository
import com.cjproductions.vicinity.location.domain.model.CountryInfo
import com.cjproductions.vicinity.location.domain.model.DefaultLocation
import com.cjproductions.vicinity.location.domain.model.LocationError
import com.cjproductions.vicinity.location.domain.model.UserLocation
import com.cjproductions.vicinity.location.domain.model.UserSetLocation
import com.cjproductions.vicinity.support.tools.AppDispatcher
import com.russhwolf.settings.ExperimentalSettingsApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.ExperimentalSerializationApi

class DefaultLocationRepository(
  private val locationDataSource: LocationDataSource,
  private val applicationScope: CoroutineScope,
  private val appDispatcher: AppDispatcher,
): LocationRepository {
  override val location: StateFlow<UserLocation?> = locationDataSource.savedLocation

  override suspend fun getLocation(): Result<DefaultLocation, LocationError> {
    return applicationScope.async(appDispatcher.io) {
      try {
        locationDataSource.getLocation()
      } catch (e: Exception) {
        println("Failed to get location: ${e.message}")
        Result.Error(LocationError.Unknown)
      }
    }.await()
  }

  override suspend fun updateUserSetCountry(countryInfo: CountryInfo) {
    val currentLocation = location.value
    val userSetLocation = UserSetLocation(
      country = countryInfo.name,
      city = countryInfo.name,
      countryCode = countryInfo.countryCode,
      latitude = countryInfo.latitude,
      longitude = countryInfo.longitude,
    )
    val userLocation = currentLocation?.copy(
      isUserSet = true,
      userSetLocation = userSetLocation,
    ) ?: UserLocation(
      isUserSet = true,
      userSetLocation = userSetLocation,
    )
    locationDataSource.saveLocation(userLocation)
  }

  override suspend fun getAllCountries() = locationDataSource.getSupportedCountries()

  override suspend fun updateDefaultLocation(defaultLocation: DefaultLocation) {
    executeWithCancellationPassthrough(
      {
        val userLocation = location.value?.copy(defaultLocation = defaultLocation)
          ?: UserLocation(defaultLocation = defaultLocation)
        locationDataSource.saveLocation(userLocation)
      }
    )
  }

  override suspend fun updateUserSetLocation(userSetLocation: UserSetLocation) {
    executeWithCancellationPassthrough(
      {
        val userLocation = location.value?.copy(
          isUserSet = true,
          userSetLocation = userSetLocation
        ) ?: UserLocation(
          isUserSet = true,
          userSetLocation = userSetLocation
        )
        locationDataSource.saveLocation(userLocation)
      }
    )
  }
}