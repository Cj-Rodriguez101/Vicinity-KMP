@file:OptIn(SupabaseExperimental::class)

package com.cjproductions.vicinity.core.data.serverless.remote

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.realtime.realtime
import kotlin.coroutines.cancellation.CancellationException

interface RemoteServerlessDataSource {
  suspend fun unsubscribeAllChannels()
}

class DefaultRemoteServerlessDataSource(
  private val supabaseClient: SupabaseClient,
): RemoteServerlessDataSource {

  override suspend fun unsubscribeAllChannels() {
    try {
      supabaseClient.realtime.subscriptions.values.forEach { it.unsubscribe() }
    } catch (ex: Exception) {
      if (ex is CancellationException) throw ex
      ex.printStackTrace()
    }
  }
}