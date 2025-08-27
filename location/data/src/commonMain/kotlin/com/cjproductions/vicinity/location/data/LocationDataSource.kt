@file:OptIn(
  ExperimentalSerializationApi::class,
  ExperimentalSettingsApi::class,
  ExperimentalCoroutinesApi::class
)

package com.cjproductions.vicinity.location.data

import com.cjproductions.vicinity.core.domain.util.Result
import com.cjproductions.vicinity.location.data.model.UserLocationDto
import com.cjproductions.vicinity.location.data.model.toUserLocation
import com.cjproductions.vicinity.location.data.model.toUserLocationDto
import com.cjproductions.vicinity.location.domain.DomainLocationTracker
import com.cjproductions.vicinity.location.domain.model.CountryInfo
import com.cjproductions.vicinity.location.domain.model.DefaultLocation
import com.cjproductions.vicinity.location.domain.model.LocationError
import com.cjproductions.vicinity.location.domain.model.UserLocation
import com.cjproductions.vicinity.support.tools.AppDispatcher
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.coroutines.getStringOrNullStateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlin.coroutines.CoroutineContext

interface LocationDataSource {
  suspend fun getLocation(): Result<DefaultLocation, LocationError>
  val savedLocation: StateFlow<UserLocation?>
  suspend fun saveLocation(userLocation: UserLocation)
  suspend fun getSupportedCountries(): List<CountryInfo>
}

class DefaultLocationDataSource(
  private val locationTracker: DomainLocationTracker,
  private val settings: ObservableSettings,
  appDispatcher: AppDispatcher,
): LocationDataSource, CoroutineScope {
  override val coroutineContext: CoroutineContext = SupervisorJob() + appDispatcher.io
  override suspend fun getLocation() = locationTracker.getLocation()


  override val savedLocation: StateFlow<UserLocation?> =
    settings.getStringOrNullStateFlow(
      coroutineScope = this,
      key = LOCATION_KEY
    ).flatMapLatest { userLocationDto ->
      flow {
        emit(
          if (userLocationDto.isNullOrEmpty()) null
          else Json.decodeFromString(
            deserializer = UserLocationDto.serializer(),
            string = userLocationDto,
          ).toUserLocation()
        )
      }
    }.catch { emit(null) }.stateIn(
      scope = this,
      started = kotlinx.coroutines.flow.SharingStarted.Eagerly,
      initialValue = null
    )

  override suspend fun saveLocation(userLocation: UserLocation) {
    settings.putString(
      LOCATION_KEY,
      Json.encodeToString(
        serializer = UserLocationDto.serializer(),
        value = userLocation.toUserLocationDto(),
      )
    )
  }

  override suspend fun getSupportedCountries() = supportedCountries
}

private val supportedCountries = listOf(
  CountryInfo(
    countryCode = "us",
    name = "United States Of America",
    latitude = 38.9072,
    longitude = -77.0369
  ),
  CountryInfo(countryCode = "ad", name = "Andorra", latitude = 42.5063, longitude = 1.5218),
  CountryInfo(countryCode = "ai", name = "Anguilla", latitude = 18.2206, longitude = -63.0686),
  CountryInfo(countryCode = "ar", name = "Argentina", latitude = -34.6118, longitude = -58.3960),
  CountryInfo(countryCode = "au", name = "Australia", latitude = -35.2809, longitude = 149.1300),
  CountryInfo(countryCode = "at", name = "Austria", latitude = 48.2082, longitude = 16.3738),
  CountryInfo(countryCode = "az", name = "Azerbaijan", latitude = 40.4093, longitude = 49.8671),
  CountryInfo(countryCode = "bs", name = "Bahamas", latitude = 25.0661, longitude = -77.3390),
  CountryInfo(countryCode = "bh", name = "Bahrain", latitude = 26.2285, longitude = 50.5860),
  CountryInfo(countryCode = "bb", name = "Barbados", latitude = 13.0935, longitude = -59.6105),
  CountryInfo(countryCode = "be", name = "Belgium", latitude = 50.8503, longitude = 4.3517),
  CountryInfo(countryCode = "bm", name = "Bermuda", latitude = 32.2942, longitude = -64.7839),
  CountryInfo(countryCode = "br", name = "Brazil", latitude = -15.8267, longitude = -47.9218),
  CountryInfo(countryCode = "bg", name = "Bulgaria", latitude = 42.6977, longitude = 23.3219),
  CountryInfo(countryCode = "ca", name = "Canada", latitude = 45.4215, longitude = -75.6972),
  CountryInfo(countryCode = "cl", name = "Chile", latitude = -33.4489, longitude = -70.6693),
  CountryInfo(countryCode = "cn", name = "China", latitude = 39.9042, longitude = 116.4074),
  CountryInfo(countryCode = "co", name = "Colombia", latitude = 4.7110, longitude = -74.0721),
  CountryInfo(countryCode = "cr", name = "Costa Rica", latitude = 9.9281, longitude = -84.0907),
  CountryInfo(countryCode = "hr", name = "Croatia", latitude = 45.8150, longitude = 15.9819),
  CountryInfo(countryCode = "cy", name = "Cyprus", latitude = 35.1856, longitude = 33.3823),
  CountryInfo(countryCode = "cz", name = "Czech Republic", latitude = 50.0755, longitude = 14.4378),
  CountryInfo(countryCode = "dk", name = "Denmark", latitude = 55.6761, longitude = 12.5683),
  CountryInfo(
    countryCode = "do",
    name = "Dominican Republic",
    latitude = 18.4861,
    longitude = -69.9312
  ),
  CountryInfo(countryCode = "ec", name = "Ecuador", latitude = -0.1807, longitude = -78.4678),
  CountryInfo(countryCode = "ee", name = "Estonia", latitude = 59.4370, longitude = 24.7536),
  CountryInfo(
    countryCode = "fo",
    name = "Faroe Islands",
    latitude = 62.0107,
    longitude = -6.7719
  ),
  CountryInfo(countryCode = "fi", name = "Finland", latitude = 60.1699, longitude = 24.9384),
  CountryInfo(countryCode = "fr", name = "France", latitude = 48.8566, longitude = 2.3522),
  CountryInfo(countryCode = "ge", name = "Georgia", latitude = 41.7151, longitude = 44.8271),
  CountryInfo(countryCode = "de", name = "Germany", latitude = 52.5200, longitude = 13.4050),
  CountryInfo(countryCode = "gh", name = "Ghana", latitude = 5.6037, longitude = -0.1870),
  CountryInfo(countryCode = "gi", name = "Gibraltar", latitude = 36.1408, longitude = -5.3536),
  CountryInfo(
    countryCode = "gb",
    name = "Great Britain",
    latitude = 51.5074,
    longitude = -0.1278
  ),
  CountryInfo(countryCode = "gr", name = "Greece", latitude = 37.9838, longitude = 23.7275),
  CountryInfo(countryCode = "hk", name = "Hong Kong", latitude = 22.3193, longitude = 114.1694),
  CountryInfo(countryCode = "hu", name = "Hungary", latitude = 47.4979, longitude = 19.0402),
  CountryInfo(countryCode = "is", name = "Iceland", latitude = 64.1466, longitude = -21.9426),
  CountryInfo(countryCode = "in", name = "India", latitude = 28.6139, longitude = 77.2090),
  CountryInfo(countryCode = "ie", name = "Ireland", latitude = 53.3498, longitude = -6.2603),
  CountryInfo(countryCode = "il", name = "Israel", latitude = 31.7683, longitude = 35.2137),
  CountryInfo(countryCode = "it", name = "Italy", latitude = 41.9028, longitude = 12.4964),
  CountryInfo(countryCode = "jm", name = "Jamaica", latitude = 17.9712, longitude = -76.7936),
  CountryInfo(countryCode = "jp", name = "Japan", latitude = 35.6762, longitude = 139.6503),
  CountryInfo(
    countryCode = "kr",
    name = "Korea, Republic of",
    latitude = 37.5665,
    longitude = 126.9780
  ),
  CountryInfo(countryCode = "lv", name = "Latvia", latitude = 56.9496, longitude = 24.1052),
  CountryInfo(countryCode = "lb", name = "Lebanon", latitude = 33.8938, longitude = 35.5018),
  CountryInfo(countryCode = "lt", name = "Lithuania", latitude = 54.6872, longitude = 25.2797),
  CountryInfo(countryCode = "lu", name = "Luxembourg", latitude = 49.6116, longitude = 6.1319),
  CountryInfo(countryCode = "my", name = "Malaysia", latitude = 3.1390, longitude = 101.6869),
  CountryInfo(countryCode = "mt", name = "Malta", latitude = 35.8989, longitude = 14.5146),
  CountryInfo(countryCode = "mx", name = "Mexico", latitude = 19.4326, longitude = -99.1332),
  CountryInfo(countryCode = "mc", name = "Monaco", latitude = 43.7384, longitude = 7.4246),
  CountryInfo(countryCode = "me", name = "Montenegro", latitude = 42.4304, longitude = 19.2594),
  CountryInfo(countryCode = "ma", name = "Morocco", latitude = 34.0209, longitude = -6.8416),
  CountryInfo(countryCode = "nl", name = "Netherlands", latitude = 52.3676, longitude = 4.9041),
  CountryInfo(
    countryCode = "an",
    name = "Netherlands Antilles",
    latitude = 12.1696,
    longitude = -68.9900
  ),
  CountryInfo(countryCode = "nz", name = "New Zealand", latitude = -41.2865, longitude = 174.7762),
  CountryInfo(
    countryCode = "nd",
    name = "Northern Ireland",
    latitude = 54.5973,
    longitude = -5.9301
  ),
  CountryInfo(countryCode = "no", name = "Norway", latitude = 59.9139, longitude = 10.7522),
  CountryInfo(countryCode = "pe", name = "Peru", latitude = -12.0464, longitude = -77.0428),
  CountryInfo(countryCode = "pl", name = "Poland", latitude = 52.2297, longitude = 21.0122),
  CountryInfo(countryCode = "pt", name = "Portugal", latitude = 38.7223, longitude = -9.1393),
  CountryInfo(countryCode = "ro", name = "Romania", latitude = 44.4268, longitude = 26.1025),
  CountryInfo(
    countryCode = "ru",
    name = "Russian Federation",
    latitude = 55.7558,
    longitude = 37.6176
  ),
  CountryInfo(countryCode = "lc", name = "Saint Lucia", latitude = 14.0101, longitude = -60.9875),
  CountryInfo(countryCode = "sa", name = "Saudi Arabia", latitude = 24.7136, longitude = 46.6753),
  CountryInfo(countryCode = "rs", name = "Serbia", latitude = 44.7866, longitude = 20.4489),
  CountryInfo(countryCode = "sg", name = "Singapore", latitude = 1.3521, longitude = 103.8198),
  CountryInfo(countryCode = "sk", name = "Slovakia", latitude = 48.1486, longitude = 17.1077),
  CountryInfo(countryCode = "si", name = "Slovenia", latitude = 46.0569, longitude = 14.5058),
  CountryInfo(countryCode = "za", name = "South Africa", latitude = -25.7479, longitude = 28.2293),
  CountryInfo(countryCode = "es", name = "Spain", latitude = 40.4168, longitude = -3.7038),
  CountryInfo(countryCode = "se", name = "Sweden", latitude = 59.3293, longitude = 18.0686),
  CountryInfo(countryCode = "ch", name = "Switzerland", latitude = 46.9480, longitude = 7.4474),
  CountryInfo(countryCode = "tw", name = "Taiwan", latitude = 25.0330, longitude = 121.5654),
  CountryInfo(countryCode = "th", name = "Thailand", latitude = 13.7563, longitude = 100.5018),
  CountryInfo(
    countryCode = "tt",
    name = "Trinidad and Tobago",
    latitude = 10.6596,
    longitude = -61.5089
  ),
  CountryInfo(countryCode = "tr", name = "Turkey", latitude = 39.9334, longitude = 32.8597),
  CountryInfo(countryCode = "ua", name = "Ukraine", latitude = 50.4501, longitude = 30.5234),
  CountryInfo(
    countryCode = "ae",
    name = "United Arab Emirates",
    latitude = 24.4539,
    longitude = 54.3773
  ),
  CountryInfo(countryCode = "uy", name = "Uruguay", latitude = -34.9011, longitude = -56.1645),
  CountryInfo(countryCode = "ve", name = "Venezuela", latitude = 10.4806, longitude = -66.9036),
)


internal const val LOCATION_KEY = "LOCATION_KEY"