package com.cjproductions.vicinity.discover.presentation.discover

import com.cjproductions.vicinity.core.domain.ClassificationName
import com.cjproductions.vicinity.discover.presentation.discover.model.ClassificationUI
import com.cjproductions.vicinity.location.domain.model.UserLocation

data class DiscoverViewState(
  val selectedSegments: List<String>? = null,
  val filterCount: Int = 0,
  val location: UserLocation? = null,
  val segments: List<ClassificationUI> = emptyList(),
)

fun ClassificationName.toClassificationUI(classificationNames: List<ClassificationName>?): ClassificationUI {
  return ClassificationUI(
    id = name,
    name = value,
    isSelected = classificationNames?.contains(this) == true,
  )
}