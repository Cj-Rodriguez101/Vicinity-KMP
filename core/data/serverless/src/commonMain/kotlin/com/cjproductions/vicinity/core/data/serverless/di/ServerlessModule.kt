package com.cjproductions.vicinity.core.data.serverless.di

import Vicinity.core.data.serverless.BuildConfig.CLIENT_ID
import Vicinity.core.data.serverless.BuildConfig.SUPABASE_API_KEY
import Vicinity.core.data.serverless.BuildConfig.SUPABASE_URL
import com.cjproductions.vicinity.core.data.serverless.DefaultServerlessRepository
import com.cjproductions.vicinity.core.data.serverless.remote.DefaultRemoteServerlessDataSource
import com.cjproductions.vicinity.core.data.serverless.remote.RemoteServerlessDataSource
import com.cjproductions.vicinity.core.domain.serverless.ServerlessRepository
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.storage.storage
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.AuthConfig
import io.github.jan.supabase.compose.auth.ComposeAuth
import io.github.jan.supabase.compose.auth.appleNativeLogin
import io.github.jan.supabase.compose.auth.googleNativeLogin
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformFirebaseModule: Module

expect fun AuthConfig.platformAuthConfig()

val coreServerlessModule = module {
  singleOf(::DefaultRemoteServerlessDataSource).bind<RemoteServerlessDataSource>()
  singleOf(::DefaultServerlessRepository).bind<ServerlessRepository>()
  single {
    createSupabaseClient(
      supabaseUrl = SUPABASE_URL,
      supabaseKey = SUPABASE_API_KEY,
    ) {
      install(Auth) { platformAuthConfig() }
      install(ComposeAuth) {
        googleNativeLogin(CLIENT_ID)
        appleNativeLogin()
      }
      install(Postgrest)
      install(Realtime)
    }
  }
  includes(platformFirebaseModule)
  single { Firebase.storage }
}