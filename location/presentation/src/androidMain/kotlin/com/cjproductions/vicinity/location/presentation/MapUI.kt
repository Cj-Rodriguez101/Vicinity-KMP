package com.cjproductions.vicinity.location.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@Composable
actual fun MapUI(
  lat: Double,
  long: Double,
  modifier: Modifier,
) {
  Box(
    modifier = modifier,
  ) {
    PrMapUI(
      lat = lat,
      long = long,
      modifier = modifier,
    )
  }
}

@Composable
private fun GoogleMapUI(
  lat: Double,
  long: Double
) {
  val coordinates = LatLng(
    /* latitude = */ lat,
    /* longitude = */ long,
  )
  val markerState = rememberMarkerState(position = coordinates)
  val cameraPositionState = rememberCameraPositionState {
    position = CameraPosition.fromLatLngZoom(coordinates, 15f)
  }
  GoogleMap(
    cameraPositionState = cameraPositionState,
  ) {
    Marker(
      state = markerState,
      title = "Bandra West", //add the real location marker
      snippet = "Mumbai"
    )
  }
}