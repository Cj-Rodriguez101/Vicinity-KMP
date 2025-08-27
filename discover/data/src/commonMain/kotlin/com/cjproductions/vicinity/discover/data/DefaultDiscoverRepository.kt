package com.cjproductions.vicinity.discover.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import app.cash.sqldelight.paging3.QueryPagingSource
import com.cjproductions.vicinity.core.data.database.VicinityDatabase
import com.cjproductions.vicinity.core.domain.ClassificationName
import com.cjproductions.vicinity.discover.data.local.LocalDiscoverDataSource
import com.cjproductions.vicinity.discover.data.local.model.toEvent
import com.cjproductions.vicinity.discover.data.paging.events.EventsRemoteMediator
import com.cjproductions.vicinity.discover.data.remote.RemoteDiscoverDataSource
import com.cjproductions.vicinity.discover.domain.DiscoverRepository
import com.cjproductions.vicinity.discover.domain.model.DiscoverEvent
import com.cjproductions.vicinity.location.domain.model.UserLocation
import com.cjproductions.vicinity.support.search.FilterParams
import com.cjproductions.vicinity.support.search.SearchParams
import com.cjproductions.vicinity.support.tools.AppDispatcher
import comcjproductionsvicinitycoredatadatabase.EventEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DefaultDiscoverRepository(
    private val appDispatcher: AppDispatcher,
    private val remoteDiscoverDataSource: RemoteDiscoverDataSource,
    private val localDiscoverDataSource: LocalDiscoverDataSource,
    private val database: VicinityDatabase,
): DiscoverRepository {
    @OptIn(ExperimentalPagingApi::class)
    override fun getEventFeed(
        searchParams: SearchParams?,
        filterParams: FilterParams?,
        location: UserLocation?,
    ): Flow<PagingData<DiscoverEvent>> {
        return Pager(
            config = PagingConfig(pageSize = 5), //we combine events with same title, so we show smaller
            remoteMediator = EventsRemoteMediator(
                searchParams = searchParams,
                filterParams = filterParams,
                remoteDiscoverDataSource = remoteDiscoverDataSource,
                localDiscoverDataSource = localDiscoverDataSource,
                location = location,
                database = database,
            ),
            pagingSourceFactory = {
                with(database.vicinityDbQueries) {
                    QueryPagingSource(
                        countQuery = countEvents(),
                        transacter = this,
                        context = appDispatcher.io,
                        queryProvider = ::events,
                    )
                }
            }
        ).flow.map { pagingData: PagingData<EventEntity> ->
            pagingData.map { eventEntity -> eventEntity.toEvent() }
        }
    }

    override val segments: List<ClassificationName> = ClassificationName.entries
}