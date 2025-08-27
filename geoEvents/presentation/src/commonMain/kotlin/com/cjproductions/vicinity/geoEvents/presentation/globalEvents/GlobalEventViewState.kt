package com.cjproductions.vicinity.geoEvents.presentation.globalEvents

import com.cjproductions.vicinity.location.domain.model.DefaultLocation
import com.cjproductions.vicinity.location.domain.model.UserLocation
import com.cjproductions.vicinity.location.domain.model.getCountry
import com.cjproductions.vicinity.location.presentation.CoordinateConverter
import com.cjproductions.vicinity.search.domain.model.GeoCodeLocation
import com.cjproductions.vicinity.search.domain.model.MapSearchEvent
import com.cjproductions.vicinity.search.domain.model.MapVenue
import com.cjproductions.vicinity.support.tools.toDateTime
import com.cjproductions.vicinity.support.tools.toLocalDateTime
import ovh.plrapps.mapcompose.ui.state.MapState

sealed class GlobalEventViewState {
    data object Loading: GlobalEventViewState()
    data object NoLocationPermission: GlobalEventViewState()
    data class Loaded(
        val totalEvents: Int = 0,
        val mapState: MapState,
        val venueByEvents: Map<String, List<MapGlobalEvent>> = mapOf(),
        val isMapLoading: Boolean = false,
        val selectedLocation: Location? = null,
        val userLocation: Location? = null,
        val searchLocations: List<GeoCodeLocation> = emptyList(),
    ): GlobalEventViewState()
}

data class Location(
    val latitude: Double,
    val longitude: Double,
    val country: String?,
)

internal fun UserLocation.toLocation(): Location {
    return userSetLocation?.let { userSet ->
        val location = CoordinateConverter.latLngToPixelXY(
            userSet.latitude ?: defaultLocation?.latitude ?: 0.0,
            userSet.longitude ?: defaultLocation?.longitude ?: 0.0,
        )
        with(location) {
            Location(
                latitude = first,
                longitude = second,
                country = getCountry(),
            )
        }
    } ?: run {
        with(defaultLocation?.toLocation()) {
            Location(
                latitude = this?.latitude ?: 0.0,
                longitude = this?.longitude ?: 0.0,
                country = getCountry(),
            )
        }
    }
}

internal fun DefaultLocation.toLocation(): Location {
    val location = CoordinateConverter.latLngToPixelXY(
        lat = latitude,
        lon = longitude,
    )

    return with(location) {
        Location(
            latitude = first,
            longitude = second,
            country = country,
        )
    }
}

data class MapBounds(
    val northEast: LatLng,
    val southWest: LatLng,
)

data class LatLng(
    val latitude: Double,
    val longitude: Double,
)

data class GlobalVenue(
    val id: String,
    val name: String,
    val location: String,
    val lat: Double,
    val lon: Double,
)

fun MapVenue.toGlobalVenue(): GlobalVenue {
    val (lat, lon) = CoordinateConverter.latLngToPixelXY(lat, lon)
    return GlobalVenue(
        id = id,
        name = name,
        location = location,
        lat = lat,
        lon = lon,
    )
}

data class MapGlobalEvent(
    val id: String,
    val name: String,
    val image: String?,
    val date: String?,
    val category: String,
    val venue: GlobalVenue,
)


fun MapSearchEvent.toMapGlobalEvent() = MapGlobalEvent(
    id = id,
    name = name,
    venue = venue!!.toGlobalVenue(),
    category = category,
    image = image,
    date = date?.toLocalDateTime()?.toDateTime()
        ?.let { dateTime -> "${dateTime.date}\n${dateTime.time}" },
)

fun List<MapSearchEvent>.toMapGlobalEvents() = map { it.toMapGlobalEvent() }
