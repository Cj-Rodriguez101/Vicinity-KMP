package com.cjproductions.vicinity.discover.data.local

import com.cjproductions.vicinity.core.data.database.DatabaseDriver
import com.cjproductions.vicinity.core.data.database.VicinityDatabase
import com.cjproductions.vicinity.discover.data.local.model.EventDataEntity
import com.cjproductions.vicinity.support.tools.normalizeText
import comcjproductionsvicinitycoredatadatabase.EventEntity

interface LocalDiscoverDataSource {
  fun resetEventsTable()
  fun insertEventRemoteKey(id: String, nextPageKey: Long?)
  fun insertEvents(events: List<EventDataEntity>)
  fun insertEvent(event: EventDataEntity)
  fun getNextPageEventKey(): Int?
  fun getEvent(title: String, category: String): EventEntity?
}

class DefaultLocalDiscoverDataSource(
  private val database: VicinityDatabase,
  private val driver: DatabaseDriver,
): LocalDiscoverDataSource {
  override fun resetEventsTable() {
    runCatching {
      database.vicinityDbQueries.deleteAllEvents()
      driver.driver.execute(
        identifier = null,
        sql = RESET_EVENT_INDICES,
        parameters = 0
      )
      driver.driver.execute(
        identifier = null,
        sql = RESET_DATE_INDICES,
        parameters = 0
      )
    }
  }

  override fun insertEventRemoteKey(id: String, nextPageKey: Long?) {
    runCatching {
      database.vicinityDbQueries.insertRemoteEventKey(
        id = NEXT_PAGE_KEY,
        nextKey = nextPageKey,
      )
    }
  }

  override fun insertEvents(events: List<EventDataEntity>) {
    runCatching {
      database.vicinityDbQueries.transaction {
        events.forEach { event ->
          database.vicinityDbQueries.insertEvent(
            normalizedTitle = event.originalTitle.normalizeText(),
            category = event.category,
            originalTitle = event.originalTitle,
            venueIds = event.venueIds,
            eventIds = event.eventIds,
            imageUrl = event.imageUrl,
            marketIds = event.marketIds,
            earliestDate = event.earliestDate,
            createdAt = event.createdAt,
          )
        }
      }
    }
  }

  override fun insertEvent(event: EventDataEntity) {
    runCatching {
      database.vicinityDbQueries.insertEvent(
        normalizedTitle = event.originalTitle.normalizeText(),
        category = event.category,
        originalTitle = event.originalTitle,
        venueIds = event.venueIds,
        eventIds = event.eventIds,
        imageUrl = event.imageUrl,
        earliestDate = event.earliestDate,
        marketIds = event.marketIds,
        createdAt = event.createdAt,
      )
    }
  }

  override fun getNextPageEventKey(): Int? {
    return database.vicinityDbQueries.getRemoteEventKeyById(NEXT_PAGE_KEY)
      .executeAsOneOrNull()?.nextKey?.toInt()
  }

  override fun getEvent(
    title: String,
    category: String,
  ): EventEntity? {
    return database.vicinityDbQueries.getEvent(
      normalizedTitle = title.normalizeText(),
      category = category,
    ).executeAsOneOrNull()
  }

  companion object {
    internal const val NEXT_PAGE_KEY = "next_page_key"
    private const val RESET_EVENT_INDICES =
      "UPDATE sqlite_sequence SET seq = 0 WHERE name = 'EventEntity'"
    private const val RESET_DATE_INDICES =
      "UPDATE sqlite_sequence SET seq = 0 WHERE name = 'DateEntity'"
  }
}