package com.cjproductions.vicinity.location.presentation

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.core.net.toUri
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import vicinity.location.presentation.generated.resources.Res
import vicinity.location.presentation.generated.resources.no_map_app_found

actual class MapController(
  val context: Context,
) {
  actual fun openMapCoordinates(lat: Double, long: Double) {
    val mapUri = "geo:$lat,$long?q=$lat,$long".toUri()
    val mapIntent = Intent(Intent.ACTION_VIEW, mapUri).apply {
      flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    if (mapIntent.resolveActivity(context.packageManager) != null) {
      mapIntent.setPackage(GOOGLE_MAPS_PACKAGE_NAME);
      startActivity(context, mapIntent, null)
    } else {
      CoroutineScope(Dispatchers.Main).launch {
        try {
          Toast.makeText(
            context,
            getString(Res.string.no_map_app_found),
            Toast.LENGTH_SHORT
          ).show()
        } catch (e: Exception) {
          println(e)
        }
      }
    }
  }

  companion object {
    private const val GOOGLE_MAPS_PACKAGE_NAME = "com.google.android.apps.maps"
  }
}