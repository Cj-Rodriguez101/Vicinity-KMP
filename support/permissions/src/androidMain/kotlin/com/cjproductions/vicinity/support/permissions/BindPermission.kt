package com.cjproductions.vicinity.support.permissions

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LifecycleOwner
import org.koin.compose.koinInject

@Composable
actual fun BindPermission() {
  val permissionsController = koinInject<SupportPermissionController>()
  val lifecycleOwner: LifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
  val context: Context = LocalContext.current

  LaunchedEffect(permissionsController, lifecycleOwner, context) {
    val activity: ComponentActivity = checkNotNull(context as? ComponentActivity) {
      "$context context is not instance of ComponentActivity"
    }
    permissionsController.bind(activity)
  }
}