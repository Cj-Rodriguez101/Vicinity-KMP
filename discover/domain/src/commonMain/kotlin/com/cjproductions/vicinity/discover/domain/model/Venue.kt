package com.cjproductions.vicinity.discover.domain.model

data class Venue(
  val name: String,
  val id: String,
  val marketIds: List<String>,
  val city: String?,
  val images: List<Image>?,
  val state: String?,
  val country: String?,
  val location: VLocation?,
)
