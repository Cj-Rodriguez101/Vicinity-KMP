package com.cjproductions.vicinity.search.domain

import com.cjproductions.vicinity.search.domain.model.RecentSearch
import com.cjproductions.vicinity.support.search.SearchFilterParamsRepository
import com.cjproductions.vicinity.support.search.SearchParams

class SearchAndUpdateUseCase(
  private val searchRepository: SearchRepository,
  private val searchFilterParamsRepository: SearchFilterParamsRepository,
) {
  suspend operator fun invoke(searchQuery: RecentSearch) {
    val updatedSearch =
      if (searchQuery.location?.isNotEmpty() == true && searchQuery.latitude == null) {
        val location =
          searchRepository.fetchLocations(searchQuery.location).getOrNull()?.firstOrNull()
        searchQuery.copy(
          latitude = location?.latitude,
          longitude = location?.longitude
        )
      } else searchQuery

    searchRepository.saveQuery(updatedSearch)
    searchFilterParamsRepository.updateSearchParams(updatedSearch.toSearchParams())
  }
}

internal fun RecentSearch.toSearchParams() = SearchParams(
  keyword = name,
  normalizedName = normalizedName,
  location = location,
  latitude = latitude,
  longitude = longitude,
)