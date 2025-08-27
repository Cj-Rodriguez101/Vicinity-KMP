package com.cjproductions.vicinity.eventDetail.presentation.eventDetail.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.XSmall
import com.cjproductions.vicinity.core.presentation.ui.components.DefaultLabeledIcon
import com.cjproductions.vicinity.core.presentation.ui.theme.VicinityTheme
import com.cjproductions.vicinity.eventDetail.presentation.eventDetail.model.VenueUi
import compose.icons.FontAwesomeIcons
import compose.icons.LineAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.MapMarkerAlt
import compose.icons.lineawesomeicons.ArrowRightSolid

@Composable
fun VenueCard(
  venue: VenueUi,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Card(
    modifier = modifier
      .padding(
        horizontal = GlobalPaddingAndSize.Medium.dp,
        vertical = XSmall.dp,
      )
      .clickable { onClick() }
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(GlobalPaddingAndSize.Medium.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Column(
        verticalArrangement = Arrangement.spacedBy(XSmall.dp)
      ) {
        Text(
          text = venue.name,
          style = MaterialTheme.typography.headlineSmall
        )

        Row {
          DefaultLabeledIcon(
            leadingIcon = FontAwesomeIcons.Solid.MapMarkerAlt,
            text = "${venue.country}"
          )
        }
      }

      Icon(
        imageVector = LineAwesomeIcons.ArrowRightSolid,
        modifier = Modifier.size(GlobalPaddingAndSize.Large.dp),
        contentDescription = null,
      )
    }
  }
}

@Preview
@Composable
fun VenueCardPreview() {
  VicinityTheme {
    VenueCard(
      venue = VenueUi(
        name = "Madelyn Robbins",
        city = "Golden Creek",
        state = "Louisiana",
        country = "Cape Verde",
        distance = "blandit",
        location = null,
        id = "",
        images = null,
      ),
      onClick = {}
    )
  }
}