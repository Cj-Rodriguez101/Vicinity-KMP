package com.cjproductions.vicinity.eventDetail.presentation.eventDetail.model

import com.cjproductions.vicinity.discover.domain.model.Attraction
import com.cjproductions.vicinity.discover.domain.model.Classification
import com.cjproductions.vicinity.discover.domain.model.Status
import com.cjproductions.vicinity.discover.domain.model.Venue
import com.cjproductions.vicinity.eventDetail.domain.EventDetail
import com.cjproductions.vicinity.location.domain.model.DefaultLocation
import kotlinx.datetime.LocalDateTime

data class EventUi(
  val id: String,
  val title: String,
  val locale: String,
  val distance: String?,
  val url: String? = null,
  val info: String? = null,
  val pleaseNote: String? = null,
  val mainImage: String? = null,
  val galleryImages: List<String>? = null,
  val dateTimeRange: LocalDateTime?,
  val status: Status,
  val classifications: List<Classification>,
  val venues: List<Venue>? = null,
  val attractions: List<Attraction>? = null,
)

fun EventDetail.toEventUi(
  defaultLocation: DefaultLocation? = null, //maybe show distance from current location later
): EventUi {
  val attractionImage = attractions
    .mapNotNull { it.images }
    .flatten()
    .filter { it.ratio == "16_9" }
    .maxByOrNull { it.width }?.url
  val venueImage = venues
    .mapNotNull { it.images }
    .flatten()
    .filter { it.ratio == "16_9" }
    .maxByOrNull { it.width }?.url
  return EventUi(
    id = id,
    title = title,
    locale = locale,
    url = url,
    info = info,
    pleaseNote = pleaseNote,
    mainImage = mainImage,
    galleryImages = listOfNotNull(attractionImage, venueImage).takeIf { it.isNotEmpty() },
    dateTimeRange = startDateTime,
    status = status,
    classifications = classifications,
    venues = venues,
    distance = null,
  )
}
