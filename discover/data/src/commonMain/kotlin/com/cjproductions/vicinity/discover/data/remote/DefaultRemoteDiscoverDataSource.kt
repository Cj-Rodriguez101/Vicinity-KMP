package com.cjproductions.vicinity.discover.data.remote

import Vicinity.discover.data.BuildConfig.BASE_URL
import com.cjproductions.vicinity.core.domain.util.DataError.Network
import com.cjproductions.vicinity.core.domain.util.Result
import com.cjproductions.vicinity.core.domain.util.map
import com.cjproductions.vicinity.discover.data.remote.model.TicketMasterEventsDto
import com.cjproductions.vicinity.discover.data.remote.model.mapToDomain
import com.cjproductions.vicinity.discover.data.remote.routes.EVENTS
import com.cjproductions.vicinity.discover.domain.model.DiscoverFeed
import com.vicinity.core.data.network.get
import io.ktor.client.HttpClient

interface RemoteDiscoverDataSource {
  suspend fun searchDiscoverFeed(
    query: Map<String, Any?>,
  ): Result<DiscoverFeed, Network>
}


class DefaultRemoteDiscoverDataSource(
  private val httpClient: HttpClient,
): RemoteDiscoverDataSource {
  override suspend fun searchDiscoverFeed(query: Map<String, Any?>): Result<DiscoverFeed, Network> {
    return httpClient.get<TicketMasterEventsDto>(
      route = EVENTS,
      baseUrl = BASE_URL,
      queryParameters = query
    ).map { discoverDto -> discoverDto.mapToDomain() }
  }
}