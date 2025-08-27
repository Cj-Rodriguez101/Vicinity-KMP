package com.cjproductions.vicinity.core.domain

enum class ClassificationName(val value: String) {
  Miscellaneous("Miscellaneous"),
  Sports("Sports"),
  Music("Music"),
  ArtsAndTheatre("Arts & Theatre"),
  Film("Film");

  companion object {
    private val valueMap = entries.associateBy { it.value }

    fun fromValue(value: String) = valueMap[value]
  }
}