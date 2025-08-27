package com.cjproductions.vicinity.location.domain

import com.cjproductions.vicinity.core.domain.util.Result
import com.cjproductions.vicinity.location.domain.model.CountryInfo
import com.cjproductions.vicinity.location.domain.model.DefaultLocation
import com.cjproductions.vicinity.location.domain.model.LocationError
import com.cjproductions.vicinity.location.domain.model.UserLocation
import com.cjproductions.vicinity.location.domain.model.UserSetLocation
import kotlinx.coroutines.flow.StateFlow

interface LocationRepository {
  val location: StateFlow<UserLocation?>
  suspend fun getLocation(): Result<DefaultLocation, LocationError>
  suspend fun updateDefaultLocation(defaultLocation: DefaultLocation)
  suspend fun updateUserSetLocation(userSetLocation: UserSetLocation)
  suspend fun updateUserSetCountry(countryInfo: CountryInfo)
  suspend fun getAllCountries(): List<CountryInfo>
}