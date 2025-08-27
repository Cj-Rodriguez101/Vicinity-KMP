package com.cjproductions.vicinity.likes.presentation.likes

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize
import com.cjproductions.vicinity.core.presentation.ui.components.ImageErrorContainer
import com.cjproductions.vicinity.core.presentation.ui.components.ImageLoadingContainer
import com.cjproductions.vicinity.core.presentation.ui.components.LikeButton
import com.cjproductions.vicinity.core.presentation.ui.theme.VicinityTheme
import com.cjproductions.vicinity.likes.domain.LikeState
import com.cjproductions.vicinity.support.share.presentation.ShareButton
import com.cjproductions.vicinity.support.tools.getCurrentTime
import com.cjproductions.vicinity.support.tools.toDateTime
import com.cjproductions.vicinity.support.tools.toLocalDateTime

@Composable
fun LikeCard(
  like: LikeUI,
  onItemClick: () -> Unit,
  onLikeClick: () -> Unit,
  onShareClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val date = remember(like.startDateTime) {
    like.startDateTime?.toDateTime()
      ?.let { dateTime -> "${dateTime.date} ${dateTime.time}" }
  }
  val imageModifier = Modifier.size(100.dp).clip(MaterialTheme.shapes.medium)
  Row(
    horizontalArrangement = Arrangement.spacedBy(GlobalPaddingAndSize.Medium.dp),
    modifier = modifier.fillMaxWidth().clickable { onItemClick() }
  ) {
    SubcomposeAsyncImage(
      model = like.image,
      contentScale = ContentScale.FillBounds,
      contentDescription = null,
      loading = { ImageLoadingContainer(modifier = imageModifier) },
      error = { ImageErrorContainer(modifier = imageModifier) },
      modifier = imageModifier,
    )

    Column(
      modifier = Modifier.weight(1f)
    ) {
      Text(
        text = like.title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
      )

      date?.let {
        Text(
          text = date,
          style = MaterialTheme.typography.bodySmall,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
        )
      }
    }

    Column(verticalArrangement = Arrangement.spacedBy(GlobalPaddingAndSize.Medium.dp)) {
      LikeButton(
        likeState = LikeState.Show(true),
        onLikeClick = onLikeClick,
        buttonColor = MaterialTheme.colorScheme.onSurface,
      )

      ShareButton(onClick = onShareClick)
    }
  }
}

@Preview
@Composable
fun LikeCardPreview() {
  VicinityTheme {
    LikeCard(
      like = LikeUI(
        userId = "12345",
        title = "Test Title",
        normalizedTitle = "test-title",
        startDateTime = getCurrentTime().toLocalDateTime(),
        category = "Test Category",
        image = null
      ),
      onItemClick = {},
      onLikeClick = {},
      onShareClick = {},
    )
  }
}