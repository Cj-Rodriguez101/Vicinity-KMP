@file:OptIn(ExperimentalForeignApi::class)

package com.cjproductions.vicinity.location.presentation

import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreLocation.CLLocationCoordinate2DMake
import platform.Foundation.NSURL
import platform.MapKit.MKMapItem
import platform.MapKit.MKPlacemark
import platform.UIKit.UIApplication

actual class MapController() {
  actual fun openMapCoordinates(lat: Double, long: Double) {
    val coordinate = CLLocationCoordinate2DMake(lat, long)
    val placemark = MKPlacemark(coordinate = coordinate)
    val mapItem = MKMapItem(placemark = placemark)

    val success = MKMapItem.openMapsWithItems(
      listOf(mapItem),
      launchOptions = null
    )

    if (success) return

    val googleMapsUrlString = "comgooglemaps://?q=$lat,$long"
    val googleMapsUrl = NSURL.URLWithString(googleMapsUrlString)

    if (googleMapsUrl != null && UIApplication.sharedApplication.canOpenURL(googleMapsUrl)) {
      UIApplication.sharedApplication.openURL(googleMapsUrl)
      return
    }

    val mapsUrlString = "maps://?q=$lat,$long"
    val mapsUrl = NSURL.URLWithString(mapsUrlString)

    if (mapsUrl != null && UIApplication.sharedApplication.canOpenURL(mapsUrl)) {
      UIApplication.sharedApplication.openURL(mapsUrl)
    }
  }
}