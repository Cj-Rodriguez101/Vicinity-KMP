package com.cjproductions.vicinity.location.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.Large
import com.cjproductions.vicinity.core.presentation.ui.components.RadiusCardContainer
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Globe
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.koinInject
import ovh.plrapps.mapcompose.api.ExperimentalClusteringApi
import ovh.plrapps.mapcompose.api.addCallout
import ovh.plrapps.mapcompose.api.addLayer
import ovh.plrapps.mapcompose.api.addMarker
import ovh.plrapps.mapcompose.api.removeCallout
import ovh.plrapps.mapcompose.api.scale
import ovh.plrapps.mapcompose.api.scrollTo
import ovh.plrapps.mapcompose.ui.MapUI
import ovh.plrapps.mapcompose.ui.state.MapState
import vicinity.core.presentation.ui.generated.resources.Marker
import vicinity.core.presentation.ui.generated.resources.Res
import kotlin.math.PI
import kotlin.math.atan
import kotlin.math.exp
import kotlin.math.ln
import kotlin.math.pow
import kotlin.math.tan

@OptIn(ExperimentalClusteringApi::class)
@Composable
fun PrMapUI(
  lat: Double,
  long: Double,
  modifier: Modifier,
) {
  val tileStreamProvider = koinInject<MapTileProvider>()
  val mapController = koinInject<MapController>()
  val coordinates = remember(lat, long) { CoordinateConverter.latLngToPixelXY(lat, long) }

  val state = remember {
    val maxLevel = 16
    val mapSize = 256 * 2.0.pow(maxLevel).toInt()
    val mapState = MapState(
      levelCount = maxLevel + 1,
      fullWidth = mapSize,
      fullHeight = mapSize,
      workerCount = 16
    ) {
      with(coordinates) {
        scroll(
          x = first,
          y = second,
        )
      }
    }
    mapState.apply {
      addLayer(tileStreamProvider)
      with(coordinates) {
        addMarker(
          id = "marker",
          x = first,
          y = second,
        ) {
          Icon(
            imageVector = vectorResource(Res.drawable.Marker),
            contentDescription = null,
            modifier = Modifier.size(Large.dp).clickable {
              mapState.addCallout(
                id = "venue",
                x = first,
                y = second
              ) {
                RadiusCardContainer(
                  modifier = Modifier.wrapContentSize(),
                ) {
                  Icon(
                    imageVector = FontAwesomeIcons.Solid.Globe,
                    contentDescription = null,
                    modifier = Modifier.size(Large.dp).clickable {
                      mapController.openMapCoordinates(
                        lat = lat,
                        long = long,
                      )
                      mapState.removeCallout("venue")
                    },
                  )
                }
              }
            },
            tint = MaterialTheme.colorScheme.error,
          )
        }
      }
      scale = 15.0
    }
    mapState
  }


  MapUI(
    modifier = modifier,
    state = state,
  ) {
    Box(
      contentAlignment = Alignment.BottomEnd,
      modifier = Modifier.fillMaxSize()
    ) {
      val scope = rememberCoroutineScope()
      LocationPickButton(
        onClick = {
          scope.launch {
            with(coordinates) {
              state.scrollTo(
                x = first,
                y = second,
              )
            }
          }
        }
      )
    }
  }
}

object CoordinateConverter {
  private const val X0 = 20037508.3427892476 // R × PI where R is Earth's radius in meters

  fun latLngToPixelXY(lat: Double, lon: Double): Pair<Double, Double> {
    val mercatorCoordinates = latLngToWebMercator(lat, lon)

    val x = (mercatorCoordinates.first + X0) / (2 * X0)
    val y = (X0 - mercatorCoordinates.second) / (2 * X0)

    return Pair(x, y)
  }

  fun normalizedToLatLng(x: Double, y: Double): Pair<Double, Double> {
    val mercatorX = x * (2 * X0) - X0
    val mercatorY = X0 - y * (2 * X0)

    return webMercatorToLatLng(mercatorX, mercatorY)
  }

  private fun latLngToWebMercator(lat: Double, lon: Double): Pair<Double, Double> {
    // Clamp latitude to valid Web Mercator range
    val clampedLat = lat.coerceIn(-85.05112878, 85.05112878)

    val x = lon * PI * 6378137.0 / 180.0 // R = 6378137.0 meters (Earth's radius)

    val latRad = clampedLat * PI / 180.0
    val y = 6378137.0 * ln(tan(PI / 4.0 + latRad / 2.0))

    return Pair(x, y)
  }

  private fun webMercatorToLatLng(x: Double, y: Double): Pair<Double, Double> {
    val earthRadius = 6378137.0

    val lon = x * 180.0 / (PI * earthRadius)

    val lat = (2.0 * atan(exp(y / earthRadius)) - PI / 2.0) * 180.0 / PI

    return Pair(lat, lon)
  }
}
