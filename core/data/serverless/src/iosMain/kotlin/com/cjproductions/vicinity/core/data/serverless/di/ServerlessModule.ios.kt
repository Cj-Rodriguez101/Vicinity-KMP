package com.cjproductions.vicinity.core.data.serverless.di

import io.github.jan.supabase.auth.AuthConfig
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformFirebaseModule: Module = module {}
actual fun AuthConfig.platformAuthConfig() {
  host = HOST
  scheme = SCHEME
}

private const val SCHEME = "auth"
private const val HOST = "vicinityltd"