package com.cjproductions.vicinity.auth.presentation.onboarding

import com.cjproductions.vicinity.core.presentation.designsystem.components.UIText
import org.jetbrains.compose.resources.DrawableResource
import vicinity.auth.presentation.generated.resources.Res
import vicinity.auth.presentation.generated.resources.concert
import vicinity.auth.presentation.generated.resources.connect_with_friends
import vicinity.auth.presentation.generated.resources.get_personalized_updates
import vicinity.auth.presentation.generated.resources.keep_track
import vicinity.auth.presentation.generated.resources.people
import vicinity.auth.presentation.generated.resources.phone
import vicinity.auth.presentation.generated.resources.receive_notifications
import vicinity.auth.presentation.generated.resources.save_favorite_events
import vicinity.auth.presentation.generated.resources.see_which_events

data class PageItem(
  val title: UIText,
  val description: UIText,
  val image: DrawableResource,
)

internal val pageItems = listOf<PageItem>(
  PageItem(
    title = UIText.StringResourceId(Res.string.save_favorite_events),
    description = UIText.StringResourceId(Res.string.keep_track),
    image = Res.drawable.people
  ),
  PageItem(
    title = UIText.StringResourceId(Res.string.get_personalized_updates),
    description = UIText.StringResourceId(Res.string.receive_notifications),
    image = Res.drawable.concert
  ),
  PageItem(
    title = UIText.StringResourceId(Res.string.connect_with_friends),
    description = UIText.StringResourceId(Res.string.see_which_events),
    image = Res.drawable.phone,
  ),
)
