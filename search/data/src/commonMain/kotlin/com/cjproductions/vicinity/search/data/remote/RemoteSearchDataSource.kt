@file:OptIn(ExperimentalTime::class)

package com.cjproductions.vicinity.search.data.remote

import Vicinity.search.data.BuildConfig.LIQ_API_KEY
import Vicinity.search.data.BuildConfig.LIQ_BASE_URL
import Vicinity.search.data.BuildConfig.TM_API_KEY
import Vicinity.search.data.BuildConfig.TM_BASE_URL
import com.cjproductions.vicinity.core.domain.ClassificationName
import com.cjproductions.vicinity.core.domain.util.DataError
import com.cjproductions.vicinity.core.domain.util.Result
import com.cjproductions.vicinity.core.domain.util.map
import com.cjproductions.vicinity.search.data.remote.model.GeoCodeLocationDto
import com.cjproductions.vicinity.search.data.remote.model.SearchEventDto
import com.cjproductions.vicinity.search.data.remote.model.toGeoCodeLocations
import com.cjproductions.vicinity.search.data.remote.model.toSearchEvent
import com.cjproductions.vicinity.search.domain.model.GeoCodeLocation
import com.cjproductions.vicinity.search.domain.model.SearchEvent
import com.vicinity.core.data.network.get
import io.ktor.client.HttpClient
import kotlin.time.ExperimentalTime

interface RemoteSearchDataSource {
  suspend fun fetchLocations(query: String): Result<List<GeoCodeLocation>, DataError.Network>
  suspend fun fetchEventsBasedOnKeyword(keyword: String): Result<SearchEvent, DataError.Network>
  suspend fun fetchEventsBasedOnGeoPoint(geoPoint: String): Result<SearchEvent, DataError.Network>
}

class DefaultRemoteSearchDataSource(
  private val httpClient: HttpClient,
): RemoteSearchDataSource {
  override suspend fun fetchLocations(query: String): Result<List<GeoCodeLocation>, DataError.Network> {
    val queryParams = buildMap {
      put(
        key = "q",
        value = query,
      )
      put(
        key = "key",
        value = LIQ_API_KEY,
      )
      put(
        key = "limit",
        value = 10,
      )
      put(
        key = "format",
        value = "json",
      )
    }
    return httpClient.get<List<GeoCodeLocationDto>>(
      route = LIQ_ROUTE,
      baseUrl = LIQ_BASE_URL,
      queryParameters = queryParams
    ).map { geoCodeLocationDto -> geoCodeLocationDto.toGeoCodeLocations() }
  }

  override suspend fun fetchEventsBasedOnKeyword(keyword: String): Result<SearchEvent, DataError.Network> {
    val currentTime = kotlin.time.Clock.System.now().toString()
    val formattedDateTime = currentTime.substringBefore('.') + "Z"
    val query = buildMap {
      put(
        key = "apikey",
        value = TM_API_KEY,
      )
      put(
        key = "keyword",
        value = keyword,
      )
      put(
        key = "size",
        value = 20,
      )
      put(
        key = "startDateTime",
        value = formattedDateTime,
      )
    }
    return httpClient.get<SearchEventDto>(
      route = TM_ROUTE,
      baseUrl = TM_BASE_URL,
      queryParameters = query
    ).map { searchEventDto -> searchEventDto.toSearchEvent() }
  }

  override suspend fun fetchEventsBasedOnGeoPoint(geoPoint: String): Result<SearchEvent, DataError.Network> {
    val currentTime = kotlin.time.Clock.System.now().toString()
    val formattedDateTime = currentTime.substringBefore('.') + "Z"
    val query = buildMap {
      put(
        key = "apikey",
        value = TM_API_KEY,
      )
      put(
        key = "geoPoint",
        value = geoPoint,
      )
      put(
        key = "size",
        value = 100,
      )
      put(
        key = "startDateTime",
        value = formattedDateTime,
      )
      put(
        key = "radius",
        value = 1,
      )

      put(
        key = "unit",
        value = "km",
      )

      put(
        key = "classificationName",
        value = ClassificationName.entries.toList(),
      )
    }
    println("Query is bucket $query")
    return httpClient.get<SearchEventDto>(
      route = TM_ROUTE,
      baseUrl = TM_BASE_URL,
      queryParameters = query
    ).map { searchEventDto -> searchEventDto.toSearchEvent() }
  }

}

private const val LIQ_ROUTE = "v1/autocomplete"
private const val TM_ROUTE = "/discovery/v2/events.json"