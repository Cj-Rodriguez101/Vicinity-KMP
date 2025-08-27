package com.cjproductions.vicinity.discover.presentation.discover.model

import androidx.compose.ui.graphics.vector.ImageVector
import com.cjproductions.vicinity.discover.domain.model.Classification
import com.cjproductions.vicinity.discover.domain.model.DiscoverEvent
import com.cjproductions.vicinity.likes.domain.LikeState
import com.cjproductions.vicinity.location.domain.model.UserLocation
import com.cjproductions.vicinity.location.domain.model.getCountryCode
import com.cjproductions.vicinity.support.tools.toDateTime
import com.cjproductions.vicinity.support.tools.toLocalDateTime
import dev.carlsen.flagkit.FlagKit

data class LocationUi(
  val city: String?,
  val country: String?,
  val image: ImageVector?,
)

fun UserLocation.toLocationUi(): LocationUi {
  return LocationUi(
    city = getSelectedCity(),
    country = getSelectedCountry(),
    image = FlagKit.getFlag(getCountryCode().orEmpty()),
  )
}

private fun UserLocation.getSelectedCountry(): String? {
  return userSetLocation?.country.takeIf { isUserSet } ?: defaultLocation?.country
}

private fun UserLocation.getSelectedCity(): String? {
  return userSetLocation?.city.takeIf { isUserSet } ?: defaultLocation?.city
}

data class DiscoverEventUi(
  val title: String,
  val normalizedTitle: String,
  val url: String? = null,
  val image: String? = null,
  val startDateTime: String?,
  val startTimestamp: Double?,
  val endTimestamp: Double?,
  val eventIds: List<String>,
  val venueIds: List<String>,
  val likeState: LikeState,
  val classification: Classification?,
)

fun DiscoverEvent.toDiscoverEventUi(
  likeState: LikeState,
  userLocation: UserLocation?,
): DiscoverEventUi {
  val startLocalDateTime = earliestDate?.toLocalDateTime()
  return DiscoverEventUi(
    title = title,
    normalizedTitle = normalizedTitle,
    url = imageUrl,
    image = imageUrl,
    startDateTime = startLocalDateTime?.toDateTime()?.run { "$date · $time" },
    startTimestamp = earliestDate,
    endTimestamp = latestDate,
    venueIds = venueIds,
    eventIds = eventIds,
    likeState = likeState,
    classification = Classification(
      name = category,
    )
  )
}