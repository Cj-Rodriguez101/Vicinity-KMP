package com.cjproductions.vicinity.discover.presentation.countrySelector.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.XSmall
import com.cjproductions.vicinity.core.presentation.ui.components.FlagCircle
import com.cjproductions.vicinity.core.presentation.ui.components.RadiusCardContainer
import com.cjproductions.vicinity.core.presentation.ui.theme.LightGray
import com.cjproductions.vicinity.discover.presentation.countrySelector.CountryUI

@Composable
fun CountryCard(
  selected: Boolean,
  countryUI: CountryUI,
  onClick: () -> Unit,
) {
  RadiusCardContainer(
    backgroundColor = if (selected) LightGray else MaterialTheme.colorScheme.surface,
    modifier = Modifier
      .padding(
        start = XSmall.dp,
        end = XSmall.dp,
        bottom = XSmall.dp,
      ).clickable(onClick = onClick)
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(XSmall.dp)
    ) {
      FlagCircle(
        flag = countryUI.image,
        onClick = onClick,
      )

      Text(
        text = countryUI.name,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurface,
      )
    }
  }
}