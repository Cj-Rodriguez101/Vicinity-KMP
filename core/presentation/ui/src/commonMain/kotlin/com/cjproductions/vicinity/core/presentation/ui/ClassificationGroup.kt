package com.cjproductions.vicinity.core.presentation.ui

import com.cjproductions.vicinity.core.domain.ClassificationName
import com.cjproductions.vicinity.core.domain.ClassificationName.ArtsAndTheatre
import com.cjproductions.vicinity.core.domain.ClassificationName.Film
import com.cjproductions.vicinity.core.domain.ClassificationName.Miscellaneous
import com.cjproductions.vicinity.core.domain.ClassificationName.Music
import com.cjproductions.vicinity.core.domain.ClassificationName.Sports
import com.cjproductions.vicinity.core.presentation.designsystem.components.UIText
import com.cjproductions.vicinity.core.presentation.designsystem.components.UIText.StringResourceId
import vicinity.core.presentation.ui.generated.resources.Res
import vicinity.core.presentation.ui.generated.resources.arts_and_theatre
import vicinity.core.presentation.ui.generated.resources.film
import vicinity.core.presentation.ui.generated.resources.miscellaneous
import vicinity.core.presentation.ui.generated.resources.music
import vicinity.core.presentation.ui.generated.resources.sports

data class ClassificationGroup(
  val classificationName: ClassificationName,
  val title: UIText,
)

val FILTER_GROUPS = listOf(
  ClassificationGroup(
    classificationName = Miscellaneous,
    title = StringResourceId(Res.string.miscellaneous),
  ),
  ClassificationGroup(
    classificationName = Sports,
    title = StringResourceId(Res.string.sports),
  ),
  ClassificationGroup(
    classificationName = Music,
    title = StringResourceId(Res.string.music),
  ),
  ClassificationGroup(
    classificationName = Film,
    title = StringResourceId(Res.string.film),
  ),
  ClassificationGroup(
    classificationName = ArtsAndTheatre,
    title = StringResourceId(Res.string.arts_and_theatre),
  ),
)