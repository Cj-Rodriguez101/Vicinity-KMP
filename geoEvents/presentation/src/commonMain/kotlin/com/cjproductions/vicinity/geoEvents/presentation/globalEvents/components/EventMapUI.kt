package com.cjproductions.vicinity.geoEvents.presentation.globalEvents.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.draggable2D
import androidx.compose.foundation.gestures.rememberDraggable2DState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.cjproductions.vicinity.core.domain.ClassificationName
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.Large
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.Medium
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.XSmall
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.XXSmall
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.XXXSmall
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.XXXXLarge
import com.cjproductions.vicinity.core.presentation.ui.components.RadiusCard
import com.cjproductions.vicinity.geoEvents.presentation.globalEvents.GlobalEventViewState.Loaded
import com.cjproductions.vicinity.geoEvents.presentation.globalEvents.Location
import com.cjproductions.vicinity.geoEvents.presentation.globalEvents.MapGlobalEvent
import com.cjproductions.vicinity.location.presentation.LocationPickButton
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Circle
import compose.icons.fontawesomeicons.solid.Film
import compose.icons.fontawesomeicons.solid.FootballBall
import compose.icons.fontawesomeicons.solid.Music
import compose.icons.fontawesomeicons.solid.TheaterMasks
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import ovh.plrapps.mapcompose.api.ExperimentalClusteringApi
import ovh.plrapps.mapcompose.api.addCallout
import ovh.plrapps.mapcompose.api.addClusterer
import ovh.plrapps.mapcompose.api.addMarker
import ovh.plrapps.mapcompose.api.removeAllMarkers
import ovh.plrapps.mapcompose.api.removeCallout
import ovh.plrapps.mapcompose.api.removeClusterer
import ovh.plrapps.mapcompose.api.removeMarker
import ovh.plrapps.mapcompose.api.snapScrollTo
import ovh.plrapps.mapcompose.ui.MapUI
import ovh.plrapps.mapcompose.ui.state.MapState
import ovh.plrapps.mapcompose.ui.state.markers.model.RenderingStrategy
import vicinity.core.presentation.ui.generated.resources.Marker
import vicinity.core.presentation.ui.generated.resources.Res
import vicinity.geoevents.presentation.generated.resources.loading_events
import vicinity.geoevents.presentation.generated.resources.your_location
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import vicinity.geoevents.presentation.generated.resources.Res as LocationRes

@OptIn(ExperimentalClusteringApi::class)
@Composable
fun EventMapUI(
    uiState: Loaded,
    onCompassClick: () -> Unit,
    onCallOutClick: (eventName: String) -> Unit,
    modifier: Modifier,
) {
    val state = uiState.mapState
    uiState.selectedLocation?.let { location ->
        LaunchedEffect(uiState.selectedLocation) {
            state.snapScrollTo(
                x = location.latitude,
                y = location.longitude,
            )
        }
    }

    LaunchedEffect(uiState.venueByEvents) {
        uiState.mapState.removeAllMarkers()
        uiState.userLocation.addUserLocationMarker(state)
        uiState.venueByEvents.addEventMarkersAndClusters(
            mapState = state,
            onCallOutClick = onCallOutClick
        )
    }

    LaunchedEffect(uiState.userLocation) {
        uiState.userLocation.addUserLocationMarker(state)
    }

    MapUI(
        modifier = modifier,
        state = state,
    ) {
        Box(
            contentAlignment = Alignment.BottomEnd,
            modifier = Modifier.fillMaxSize().padding(bottom = XXXXLarge.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState.isMapLoading) {
                    Card(
                        modifier = Modifier.padding(XSmall.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                        ),
                    ) {
                        Row(
                            modifier = Modifier.padding(XSmall.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(Medium.dp),
                                strokeWidth = XXXSmall.dp,
                            )
                            Spacer(modifier = Modifier.width(XSmall.dp))
                            Text(
                                text = stringResource(LocationRes.string.loading_events),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
                LocationPickButton(
                    onClick = onCompassClick,
                    modifier = Modifier.padding(Medium.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalClusteringApi::class)
private fun Map<String, List<MapGlobalEvent>>.addEventMarkersAndClusters(
    mapState: MapState,
    onCallOutClick: (String) -> Unit,
) {
    this.forEach { (venueId, events) ->
        if (events.size == 1) {
            val event = events[0]
            val venue = event.venue

            mapState.addMarker(
                id = event.id,
                x = venue.lat,
                y = venue.lon,
            ) {
                EventMarker(
                    event = event,
                    onClick = {
                        mapState.removeCallout(event.id)
                        mapState.addCallout(
                            id = event.id,
                            x = event.venue.lat,
                            y = event.venue.lon,
                            zIndex = 15f,
                            c = {
                                EventCallOut(
                                    onCallOutClick = onCallOutClick,
                                    event = event,
                                )
                            }
                        )
                    }
                )
            }
        } else {
            mapState.removeClusterer(venueId)
            mapState.addClusterer(venueId) { ids -> { EventCluster(ids.size) } }

            events.forEachIndexed { index, event ->
                val offsetRadius = 100.dp
                val angle =
                    (index * (360.0 / events.size)) * (PI / 180.0)
                val offsetX = (offsetRadius.value * cos(angle)).dp
                val offsetY = (offsetRadius.value * sin(angle)).dp

                mapState.addMarker(
                    id = event.id,
                    x = event.venue.lat,
                    y = event.venue.lon,
                    isConstrainedInBounds = false,
                    absoluteOffset = DpOffset(offsetX, offsetY),
                    relativeOffset = Offset(-0.5f, -0.5f),
                    renderingStrategy = RenderingStrategy.Clustering(venueId),
                ) {
                    EventMarker(
                        event = event,
                        onClick = {
                            mapState.removeCallout(event.id)
                            mapState.addCallout(
                                id = event.id,
                                x = event.venue.lat,
                                y = event.venue.lon,
                                zIndex = 15f,
                                c = {
                                    EventCallOut(
                                        event = event,
                                        onCallOutClick = onCallOutClick,
                                    )
                                }
                            )
                        }
                    )
                }
            }
        }
    }
}

private fun Location?.addUserLocationMarker(
    state: MapState,
) {
    this?.let { userLocation ->
        state.removeMarker(USER_LOCATION)
        state.addMarker(
            id = USER_LOCATION,
            x = userLocation.latitude,
            y = userLocation.longitude,
        ) {
            Icon(
                imageVector = FontAwesomeIcons.Solid.Circle,
                contentDescription = null,
                modifier = Modifier
                    .size(Large.dp)
                    .clickable {
                        state.removeCallout(USER_LOCATION)
                        state.addCallout(
                            id = USER_LOCATION,
                            x = userLocation.latitude,
                            y = userLocation.longitude,
                            absoluteOffset = DpOffset(0.dp, (-24).dp),
                            zIndex = 15f,
                        ) {
                            RadiusCard {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(XSmall.dp),
                                    modifier = Modifier.padding(Medium.dp)
                                ) {
                                    Text(
                                        text = stringResource(LocationRes.string.your_location),
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Black,
                                        maxLines = 2,
                                    )
                                    Text(text = userLocation.country.orEmpty())
                                }
                            }
                        }
                    },
                tint = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
private fun EventCallOut(
    event: MapGlobalEvent,
    onCallOutClick: (String) -> Unit,
) {
    var offset by remember { mutableStateOf(Offset.Zero) }
    val draggableState = rememberDraggable2DState { offset += it }
    RadiusCard(
        modifier = Modifier
            .draggable2D(
                state = draggableState,
                enabled = true,
            ).offset {
                IntOffset(
                    x = offset.x.roundToInt(),
                    y = offset.y.roundToInt(),
                )
            }.clickable { onCallOutClick(event.name) }
            .padding(Medium.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(Medium.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(Medium.dp)
        ) {
            AsyncImage(
                model = event.image,
                placeholder = ColorPainter(Color.LightGray),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.size(100.dp).clip(MaterialTheme.shapes.medium)
            )
            Column {
                Text(
                    text = event.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    maxLines = 2,
                )

                Text(
                    text = event.venue.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                )

                Spacer(modifier = Modifier.height(XXSmall.dp))

                event.date?.let {
                    Text(
                        text = event.date,
                        style = MaterialTheme.typography.titleSmall,
                    )
                }
            }
        }
    }
}

@Composable
private fun EventMarker(
    event: MapGlobalEvent,
    onClick: (MapGlobalEvent) -> Unit,
) {
    Icon(
        imageVector = when (event.category) {
            ClassificationName.Music.value -> FontAwesomeIcons.Solid.Music
            ClassificationName.ArtsAndTheatre.value -> FontAwesomeIcons.Solid.TheaterMasks
            ClassificationName.Sports.value -> FontAwesomeIcons.Solid.FootballBall
            ClassificationName.Film.value -> FontAwesomeIcons.Solid.Film
            else -> vectorResource(Res.drawable.Marker)
        },
        contentDescription = event.name,
        modifier = Modifier.size(Large.dp).clickable { onClick(event) },
        tint = MaterialTheme.colorScheme.primary
    )
}

@Composable
private fun EventCluster(size: Int) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(50.dp)
            .background(
                MaterialTheme.colorScheme.primary,
                CircleShape
            )
    ) {
        Text(
            text = size.toString(),
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )
    }
}

private const val USER_LOCATION = "user_location"
