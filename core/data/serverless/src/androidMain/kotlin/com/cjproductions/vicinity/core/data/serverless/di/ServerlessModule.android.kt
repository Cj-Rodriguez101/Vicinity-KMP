package com.cjproductions.vicinity.core.data.serverless.di

import com.google.firebase.FirebaseApp
import io.github.jan.supabase.auth.AuthConfig
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

actual val platformFirebaseModule = module {
  single { FirebaseApp.initializeApp(androidApplication()) }
}

actual fun AuthConfig.platformAuthConfig() {
  host = HOST
  scheme = SCHEME
}

private const val SCHEME = "https"
private const val HOST = "vicinityltd.app"