package com.cjproductions.vicinity.search.data

import com.cjproductions.vicinity.core.domain.util.DataError
import com.cjproductions.vicinity.core.domain.util.Result
import com.cjproductions.vicinity.location.data.latLongToHash
import com.cjproductions.vicinity.search.data.local.LocalSearchDataSource
import com.cjproductions.vicinity.search.data.remote.RemoteSearchDataSource
import com.cjproductions.vicinity.search.domain.SearchRepository
import com.cjproductions.vicinity.search.domain.model.GeoCodeLocation
import com.cjproductions.vicinity.search.domain.model.RecentSearch
import com.cjproductions.vicinity.support.tools.AppDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

class DefaultSearchRepository(
  private val remoteDataSource: RemoteSearchDataSource,
  private val localDataSource: LocalSearchDataSource,
  private val appDispatcher: AppDispatcher,
): SearchRepository {
  override val searchItems: Flow<List<RecentSearch>> =
    localDataSource.getSearchItems()
      .catch { ex ->
        println("Failed to fetch search items $ex")
        emit(emptyList())
      }.flowOn(appDispatcher.io)

  override suspend fun fetchLocations(location: String): Result<List<GeoCodeLocation>, DataError.Network> =
    withContext(appDispatcher.io) {
      try {
        remoteDataSource.fetchLocations(location)
      } catch (e: Exception) {
        if (e is CancellationException) throw e
        println(e.toString())
        Result.Error(DataError.Network.UNKNOWN)
      }
    }

  override suspend fun fetchEventsBasedOnKeyword(keyword: String) =
    withContext(appDispatcher.io) {
      try {
        remoteDataSource.fetchEventsBasedOnKeyword(keyword)
      } catch (e: Exception) {
        if (e is CancellationException) throw e
        println(e.toString())
        Result.Error(DataError.Network.UNKNOWN)
      }
    }

  override suspend fun fetchEventsBasedOnLocation(
    latitude: Double,
    longitude: Double,
  ) = withContext(appDispatcher.io) {
    try {
      val geoHash = latLongToHash(latitude, longitude)
      //println("GeoHash: $geoHash")
      remoteDataSource.fetchEventsBasedOnGeoPoint(geoHash)
    } catch (e: Exception) {
      if (e is CancellationException) throw e
      println(e.toString())
      Result.Error(DataError.Network.UNKNOWN)
    }
  }

  override suspend fun saveQuery(recentSearch: RecentSearch) =
    withContext(appDispatcher.io) {
      try {
        localDataSource.saveRecentSearch(recentSearch)
      } catch (e: Exception) {
        println(e.toString())
      }
    }

  override suspend fun deleteQuery(recentSearch: RecentSearch) =
    withContext(appDispatcher.io) {
      try {
        localDataSource.deleteRecentSearch(recentSearch)
      } catch (e: Exception) {
        println(e.toString())
      }
    }
}