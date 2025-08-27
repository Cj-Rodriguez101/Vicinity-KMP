package com.cjproductions.vicinity.eventDetail.domain

import com.cjproductions.vicinity.discover.domain.model.Attraction
import com.cjproductions.vicinity.discover.domain.model.Classification
import com.cjproductions.vicinity.discover.domain.model.Status
import com.cjproductions.vicinity.discover.domain.model.Venue
import kotlinx.datetime.LocalDateTime

data class EventDetail(
  val id: String,
  val title: String,
  val locale: String,
  val url: String? = null,
  val info: String? = null,
  val pleaseNote: String? = null,
  val mainImage: String?,
  val otherImages: List<String>,
  val startDateTime: LocalDateTime?,
  val status: Status,
  val classifications: List<Classification>,
  val venues: List<Venue>,
  val attractions: List<Attraction>,
)