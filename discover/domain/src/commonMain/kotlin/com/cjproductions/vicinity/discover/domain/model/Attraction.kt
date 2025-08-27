package com.cjproductions.vicinity.discover.domain.model

data class Attraction(
    val name: String,
    val id: String,
    val type: String,
    val images: List<Image>?,
)
