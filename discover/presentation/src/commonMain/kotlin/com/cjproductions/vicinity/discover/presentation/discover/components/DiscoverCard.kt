package com.cjproductions.vicinity.discover.presentation.discover.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.Large
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.Medium
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.XSmall
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.XXSmall
import com.cjproductions.vicinity.core.presentation.ui.components.ImageErrorContainer
import com.cjproductions.vicinity.core.presentation.ui.components.ImageLoadingContainer
import com.cjproductions.vicinity.core.presentation.ui.components.LikeButton
import com.cjproductions.vicinity.core.presentation.ui.components.RadiusCard
import com.cjproductions.vicinity.core.presentation.ui.imageColorFilter
import com.cjproductions.vicinity.discover.domain.model.Classification
import com.cjproductions.vicinity.discover.presentation.discover.model.DiscoverEventUi
import com.cjproductions.vicinity.likes.domain.LikeState
import com.cjproductions.vicinity.support.share.presentation.getShareIcon

@Composable
fun DiscoverCard(
  event: DiscoverEventUi,
  modifier: Modifier = Modifier,
  onEventClick: () -> Unit,
  onLikeClick: () -> Unit,
  onShareClick: () -> Unit,
) {
  RadiusCard(
    modifier = modifier
      .padding(horizontal = Medium.dp)
      .clickable { onEventClick() },
  ) {
    Box(modifier = Modifier.padding(Medium.dp)) {
      Column {
        SubcomposeAsyncImage(
          model = event.image,
          contentDescription = event.title,
          contentScale = ContentScale.FillBounds,
          colorFilter = imageColorFilter,
          loading = {
            ImageLoadingContainer(
              modifier = Modifier
                .fillMaxWidth()
                .height(IMAGE_SIZE)
                .clip(MaterialTheme.shapes.medium),
            )
          },
          error = {
            ImageErrorContainer(
              modifier = Modifier
                .fillMaxWidth()
                .height(IMAGE_SIZE)
                .clip(MaterialTheme.shapes.medium)
            )
          },
          modifier = Modifier
            .fillMaxWidth()
            .height(IMAGE_SIZE)
            .clip(MaterialTheme.shapes.medium),
        )

        Spacer(Modifier.height(Medium.dp).fillMaxWidth())

        Row(
          horizontalArrangement = Arrangement.SpaceBetween,
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(
            verticalArrangement = Arrangement.spacedBy(XXSmall.dp),
            modifier = Modifier.fillMaxWidth(0.7f)
          ) {
            Text(
              text = event.title,
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              minLines = 2,
              maxLines = 2,
              overflow = TextOverflow.Ellipsis,
            )

            event.startDateTime?.let { dateTime ->
              Text(
                text = dateTime,
                fontSize = 14.sp
              )
            }
          }

          Row(horizontalArrangement = Arrangement.spacedBy(Medium.dp)) {
            (event.likeState as? LikeState.Show)?.let { isSaved ->
              LikeButton(
                likeState = isSaved,
                onLikeClick = onLikeClick,
                buttonColor = MaterialTheme.colorScheme.onBackground,
              )
            }

            Icon(
              imageVector = getShareIcon(),
              contentDescription = null,
              modifier = Modifier.size(Large.dp).clickable { onShareClick() },
            )
          }
        }
      }

      event.classification?.let {
        Box(
          contentAlignment = Alignment.Center,
          modifier = Modifier
            .align(Alignment.TopStart)
            .padding(Medium.dp)
        ) {
          RadiusCard(
            name = it.name,
            textStyle = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(XSmall.dp),
          )
        }
      }
    }
  }
}

private val IMAGE_SIZE = 200.dp

@Preview
@Composable
fun DiscoverCardPreview() {
  MaterialTheme {
    DiscoverCard(
      event = DiscoverEventUi(
        normalizedTitle = "title",
        title = "Event Title",
        url = "http://www.bing.com/search?q=vidisse",
        image = "hinc",
        startDateTime = "24/06/2025 · 07:05PM",
        likeState = LikeState.Show(false),
        venueIds = listOf(),
        eventIds = listOf(),
        startTimestamp = null,
        endTimestamp = null,
        classification = Classification(name = "Andrew Conrad")
      ),
      onEventClick = {},
      onLikeClick = {},
      onShareClick = {},
    )
  }
}