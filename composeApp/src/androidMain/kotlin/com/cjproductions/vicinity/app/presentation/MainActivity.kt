package com.cjproductions.vicinity.app.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.handleDeeplinks
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.init
import org.koin.android.ext.android.inject

class MainActivity: ComponentActivity() {
  private val supabaseClient: SupabaseClient by inject()

  override fun onNewIntent(intent: Intent) {
    super.onNewIntent(intent)
    supabaseClient.handleDeeplinks(intent)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    supabaseClient.handleDeeplinks(intent)
    FileKit.init(this)
    enableEdgeToEdge(
      statusBarStyle = SystemBarStyle.light(
        android.graphics.Color.TRANSPARENT,
        android.graphics.Color.TRANSPARENT,
      ),
      navigationBarStyle = SystemBarStyle.light(
        android.graphics.Color.TRANSPARENT,
        android.graphics.Color.TRANSPARENT,
      ),
    )
    setContent {
      App()
    }
  }
}

@Preview
@Composable
fun AppAndroidPreview() {
  App()
}