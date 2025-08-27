package com.cjproductions.vicinity.search.presentation.searchResults.components

import com.cjproductions.vicinity.discover.domain.model.Classification
import com.cjproductions.vicinity.discover.domain.model.DiscoverEvent
import com.cjproductions.vicinity.likes.domain.LikeState
import com.cjproductions.vicinity.support.tools.toDateTime
import com.cjproductions.vicinity.support.tools.toLocalDateTime

data class SearchResultUI(
  val normalizedTitle: String,
  val title: String,
  val image: String? = null,
  val startDateTime: String?,
  val startTimeStamp: Double?,
  val endTimeStamp: Double?,
  val likeState: LikeState,
  val classification: Classification?,
)

internal fun DiscoverEvent.toSearchResultUi(
  savedState: LikeState,
) = SearchResultUI(
  normalizedTitle = normalizedTitle,
  title = title,
  image = imageUrl,
  startDateTime = earliestDate?.toLocalDateTime()?.toDateTime()?.run { "$date · $time" },
  startTimeStamp = earliestDate,
  endTimeStamp = latestDate,
  likeState = savedState,
  classification = Classification(name = category),

  )


