package com.cjproductions.vicinity.discover.domain

import androidx.paging.PagingData
import com.cjproductions.vicinity.core.domain.ClassificationName
import com.cjproductions.vicinity.discover.domain.model.DiscoverEvent
import com.cjproductions.vicinity.location.domain.model.UserLocation
import com.cjproductions.vicinity.support.search.FilterParams
import com.cjproductions.vicinity.support.search.SearchParams
import kotlinx.coroutines.flow.Flow

interface DiscoverRepository {
  fun getEventFeed(
    searchParams: SearchParams? = null,
    filterParams: FilterParams? = null,
    location: UserLocation? = null,
  ): Flow<PagingData<DiscoverEvent>>

  val segments: List<ClassificationName>
}