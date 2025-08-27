package com.cjproductions.vicinity.search.presentation.search

import com.cjproductions.vicinity.search.domain.model.RecentSearch
import com.cjproductions.vicinity.support.tools.normalizeText

data class CurrentSearch(
  val keyword: String? = null,
  val location: String? = null,
  val latitude: Double? = null,
  val longitude: Double? = null,
)

internal fun CurrentSearch.toRecentSearch() = RecentSearch(
  normalizedName = keyword?.normalizeText() ?: location?.normalizeText().orEmpty(),
  name = keyword,
  location = location,
  latitude = latitude,
  longitude = longitude,
)