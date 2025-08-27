package com.cjproductions.vicinity.search.domain

import com.cjproductions.vicinity.core.domain.util.DataError
import com.cjproductions.vicinity.core.domain.util.Result
import com.cjproductions.vicinity.search.domain.model.GeoCodeLocation
import com.cjproductions.vicinity.search.domain.model.RecentSearch
import com.cjproductions.vicinity.search.domain.model.SearchEvent
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
  val searchItems: Flow<List<RecentSearch>>
  suspend fun fetchLocations(location: String): Result<List<GeoCodeLocation>, DataError.Network>
  suspend fun fetchEventsBasedOnKeyword(keyword: String): Result<SearchEvent, DataError.Network>
  suspend fun fetchEventsBasedOnLocation(
    latitude: Double,
    longitude: Double,
  ): Result<SearchEvent, DataError.Network>

  suspend fun saveQuery(recentSearch: RecentSearch)
  suspend fun deleteQuery(recentSearch: RecentSearch)
}