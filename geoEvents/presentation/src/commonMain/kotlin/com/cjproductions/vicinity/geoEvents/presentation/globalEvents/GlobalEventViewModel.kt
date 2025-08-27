@file:OptIn(ExperimentalCoroutinesApi::class)

package com.cjproductions.vicinity.geoEvents.presentation.globalEvents

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cjproductions.vicinity.core.domain.util.onFailure
import com.cjproductions.vicinity.core.domain.util.onSuccess
import com.cjproductions.vicinity.core.presentation.designsystem.components.UIText
import com.cjproductions.vicinity.core.presentation.ui.SnackBarController
import com.cjproductions.vicinity.core.presentation.ui.SnackBarEvent.SnackBarUiEvent
import com.cjproductions.vicinity.geoEvents.presentation.globalEvents.GlobalEventAction.OnCompassClick
import com.cjproductions.vicinity.geoEvents.presentation.globalEvents.GlobalEventViewState.Loaded
import com.cjproductions.vicinity.geoEvents.presentation.globalEvents.GlobalEventViewState.Loading
import com.cjproductions.vicinity.location.domain.LocationRepository
import com.cjproductions.vicinity.location.domain.model.DefaultLocation
import com.cjproductions.vicinity.location.domain.model.UserLocation
import com.cjproductions.vicinity.location.presentation.MapTileProvider
import com.cjproductions.vicinity.search.domain.SearchRepository
import com.cjproductions.vicinity.search.domain.model.SearchEvent
import com.cjproductions.vicinity.support.tools.Debouncer
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ovh.plrapps.mapcompose.api.addLayer
import ovh.plrapps.mapcompose.api.enableRotation
import ovh.plrapps.mapcompose.api.enableZooming
import ovh.plrapps.mapcompose.api.removeStateChangeListener
import ovh.plrapps.mapcompose.api.scale
import ovh.plrapps.mapcompose.api.scrollTo
import ovh.plrapps.mapcompose.api.setStateChangeListener
import ovh.plrapps.mapcompose.api.visibleBoundingBox
import ovh.plrapps.mapcompose.ui.state.MapState
import kotlin.coroutines.cancellation.CancellationException
import kotlin.math.pow

class GlobalEventViewModel(
    private val locationRepository: LocationRepository,
    private val searchRepository: SearchRepository,
    private val snackBarController: SnackBarController,
    private val mapTileProvider: MapTileProvider,
    private val deBouncer: Debouncer,
): ViewModel() {
    private val isMapLoading = MutableStateFlow(false)
    private val mapSearchEvent = MutableStateFlow(
        SearchEvent(
            events = emptyList(),
            totalEvents = 0,
        )
    )

    private val userLocation = locationRepository.location.mapLatest { userLocation ->
        userLocation ?: DEFAULT_USER_LOCATION
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = DEFAULT_USER_LOCATION,
    )
    private val mapState = createMapState()
    private val _state = MutableStateFlow<GlobalEventViewState>(Loading)
    val state = combine(
        _state,
        userLocation,
        mapSearchEvent,
        isMapLoading,
    ) { state, location, searchItem, isMapLoading ->
        location.let {
            val mapSearches = searchItem.events.filter { it.venue != null }
            val venueByEvents = mapSearches.groupBy { it.venue!!.id }

            Loaded(
                totalEvents = searchItem.events.size,
                mapState = mapState,
                venueByEvents = venueByEvents.mapValues { it.value.toMapGlobalEvents() },
                isMapLoading = isMapLoading,
                selectedLocation = location.toLocation(),
                userLocation = location.defaultLocation?.toLocation()
                    .takeIf { it?.latitude != DEFAULT_USER_LOCATION.toLocation().latitude },
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = Loading,
    )

    init {
        mapState.setStateChangeListener {
            viewModelScope.launch {
                try {
                    val mapBounds = convertBoundingBoxToMapBounds(mapState.visibleBoundingBox())
                    searchBounds(mapBounds)
                } catch (e: Exception) {
                    println("Error calculating bounds: ${e.message}")
                    if (e is CancellationException) throw e
                }
            }
        }
    }

    override fun onCleared() {
        mapState.removeStateChangeListener()
        super.onCleared()
    }

    fun onAction(action: GlobalEventAction) {
        when (action) {
            OnCompassClick -> focusUserLocation()
        }
    }

    private fun focusUserLocation() {
        isMapLoading.update { true }
        val currentLocation = userLocation.value.defaultLocation
        if (currentLocation == DEFAULT_USER_LOCATION.defaultLocation) {
            getUserLocationAndScroll()
        } else scrollToUserLocation(currentLocation)
        isMapLoading.update { false }
    }

    private fun scrollToUserLocation(currentLocation: DefaultLocation?) {
        viewModelScope.launch {
            currentLocation?.let {
                with(it.toLocation()) {
                    mapState.scrollTo(
                        x = latitude,
                        y = longitude,
                    )
                }

            }
        }
    }

    private fun getUserLocationAndScroll() {
        viewModelScope.launch {
            val location = locationRepository.getLocation().getOrNull()
            if (location == null) return@launch
            locationRepository.updateDefaultLocation(location)
            with(location.toLocation()) {
                mapState.scrollTo(
                    x = latitude,
                    y = longitude,
                )
            }
        }
    }

    private fun searchBounds(bounds: MapBounds) {
        isMapLoading.update { true }
        deBouncer.debounceAction(scope = viewModelScope) {
            val centerLat = (bounds.northEast.latitude + bounds.southWest.latitude) / 2
            val centerLon = (bounds.northEast.longitude + bounds.southWest.longitude) / 2
            searchRepository.fetchEventsBasedOnLocation(
                latitude = centerLat,
                longitude = centerLon
            ).onSuccess { mapEvents ->
                mapSearchEvent.update { mapEvents }
            }.onFailure {
                snackBarController.sendEvent(
                    SnackBarUiEvent(UIText.DynamicString("Failed to fetch events"))
                )
            }
            println("Bounds are $bounds")
            isMapLoading.update { false }
        }
    }

    private fun createMapState(): MapState {
        val maxLevel = 16
        val mapSize = 256 * 2.0.pow(maxLevel).toInt()
        val state = MapState(
            levelCount = maxLevel + 1,
            fullWidth = mapSize,
            fullHeight = mapSize,
            workerCount = 16
        ).apply {
            scale = 1.0
            addLayer(mapTileProvider)
            enableRotation()
            enableZooming()
        }
        return state
    }

    companion object {
        private val USA_DEFAULT_LOCATION = DefaultLocation(
            latitude = 40.7128,
            longitude = -73.935242,
            country = "United States",
            countryCode = "us",
            city = "New York",
        )

        private val DEFAULT_USER_LOCATION = UserLocation(defaultLocation = USA_DEFAULT_LOCATION)
    }
}