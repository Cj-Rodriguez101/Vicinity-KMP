package com.cjproductions.vicinity.search.presentation.searchResults.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import coil3.compose.SubcomposeAsyncImage
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.Medium
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.XSmall
import com.cjproductions.vicinity.core.presentation.ui.components.DefaultLabeledIcon
import com.cjproductions.vicinity.core.presentation.ui.components.ImageErrorContainer
import com.cjproductions.vicinity.core.presentation.ui.components.ImageLoadingContainer
import com.cjproductions.vicinity.core.presentation.ui.components.LikeButton
import com.cjproductions.vicinity.core.presentation.ui.components.RadiusCard
import com.cjproductions.vicinity.core.presentation.ui.theme.VicinityTheme
import com.cjproductions.vicinity.likes.domain.LikeState
import com.cjproductions.vicinity.support.share.presentation.ShareButton
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Regular
import compose.icons.fontawesomeicons.regular.CalendarAlt

@Composable
fun SearchResultCard(
  searchResult: SearchResultUI,
  onItemClick: (String) -> Unit,
  onLikeClick: (Boolean) -> Unit,
  onShareClick: (String) -> Unit,
  modifier: Modifier = Modifier,
) {
  RadiusCard(
    modifier = modifier
      .padding(horizontal = Medium.dp)
      .clickable { onItemClick(searchResult.title) }
  ) {
    Row(
      horizontalArrangement = Arrangement.spacedBy(Medium.dp),
      modifier = Modifier.padding(Medium.dp),
    ) {
      val imageModifier = Modifier
        .size(ROW_ITEM_HEIGHT.dp)
        .padding(horizontal = XSmall.dp)
        .clip(MaterialTheme.shapes.medium)

      SubcomposeAsyncImage(
        model = searchResult.image,
        contentScale = ContentScale.FillBounds,
        contentDescription = searchResult.title,
        loading = { ImageLoadingContainer(modifier = imageModifier) },
        error = { ImageErrorContainer(modifier = imageModifier) },
        modifier = imageModifier,
      )

      Column(
        verticalArrangement = Arrangement.spacedBy(XSmall.dp, Alignment.Top),
        modifier = Modifier.weight(1f),
      ) {
        with(searchResult) {
          Text(
            text = searchResult.title,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 2,
            minLines = 2,
          )

          startDateTime?.let {
            DefaultLabeledIcon(
              leadingIcon = FontAwesomeIcons.Regular.CalendarAlt,
              text = it,
              textStyle = MaterialTheme.typography.bodySmall,
            )
          }
        }
      }

      Column(
        verticalArrangement = Arrangement.spacedBy(Medium.dp),
        modifier = Modifier.align(Alignment.Top),
      ) {
        (searchResult.likeState as? LikeState.Show)?.let {
          LikeButton(
            likeState = it,
            onLikeClick = { onLikeClick(it.liked) },
            buttonColor = MaterialTheme.colorScheme.onBackground,
          )
        }

        ShareButton(onClick = { onShareClick(searchResult.title) })
      }
    }
  }
}

private const val ROW_ITEM_HEIGHT = 100

@Preview
@Composable
fun SearchResultCardPreview() {
  VicinityTheme {
    SearchResultCard(
      searchResult = SearchResultUI(
        title = "very long text that should wrap",
        normalizedTitle = "very long text that should wrap",
        image = "definiebas",
        startDateTime = "eos",
        startTimeStamp = null,
        endTimeStamp = null,
        likeState = LikeState.Show(true),
        classification = null
      ),
      onItemClick = {},
      onLikeClick = { _ -> },
      onShareClick = {},
    )
  }
}