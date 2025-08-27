package com.cjproductions.vicinity.core.data.serverless.di

import com.cjproductions.vicinity.support.tools.APP_NAME
import io.github.jan.supabase.auth.AuthConfig
import org.koin.core.module.Module
import org.koin.dsl.module
import kotlin.time.Duration.Companion.seconds

actual val platformFirebaseModule: Module = module { }
actual fun AuthConfig.platformAuthConfig() {
  httpCallbackConfig {
    htmlTitle = APP_NAME
    timeout = 60.seconds
  }
}