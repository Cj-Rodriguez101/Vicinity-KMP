package com.cjproductions.vicinity.search.data.local.model

import com.cjproductions.vicinity.search.domain.model.RecentSearch
import comcjproductionsvicinitycoredatadatabase.SearchEntity

private fun SearchEntity.toRecentSearches() = RecentSearch(
  normalizedName = normalizedName,
  name = name,
  location = location,
  latitude = latitude,
  longitude = longitude,
)

fun List<SearchEntity>.toRecentSearches() = map { it.toRecentSearches() }