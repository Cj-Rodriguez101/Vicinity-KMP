package com.cjproductions.vicinity.search.data.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.cjproductions.vicinity.core.data.database.VicinityDatabase
import com.cjproductions.vicinity.search.data.local.model.toRecentSearches
import com.cjproductions.vicinity.search.domain.model.RecentSearch
import com.cjproductions.vicinity.support.tools.AppDispatcher
import com.cjproductions.vicinity.support.tools.normalizeText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface LocalSearchDataSource {
  fun getSearchItems(): Flow<List<RecentSearch>>
  suspend fun saveRecentSearch(recentSearch: RecentSearch)
  suspend fun deleteRecentSearch(recentSearch: RecentSearch)
}

class DefaultLocalSearchDataSource(
  private val database: VicinityDatabase,
  private val appDispatcher: AppDispatcher,
): LocalSearchDataSource {
  override fun getSearchItems(): Flow<List<RecentSearch>> {
    return database.vicinityDbQueries.getSearches().asFlow().mapToList(appDispatcher.io)
      .map { it.toRecentSearches() }
  }

  override suspend fun saveRecentSearch(recentSearch: RecentSearch) {
    with(recentSearch) {
      database.vicinityDbQueries.insertSearch(
        normalizedName = name?.normalizeText() ?: location?.normalizeText().orEmpty(),
        name = name,
        location = location,
        latitude = latitude,
        longitude = longitude,
      )
    }
  }

  override suspend fun deleteRecentSearch(
    recentSearch: RecentSearch,
  ) {
    with(recentSearch) {
      database.vicinityDbQueries.deleteSpecificSearch(
        normalizedName = name?.normalizeText() ?: location?.normalizeText().orEmpty(),
      )
    }
  }
}