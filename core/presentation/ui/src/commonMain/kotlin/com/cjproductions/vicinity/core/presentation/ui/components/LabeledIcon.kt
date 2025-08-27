package com.cjproductions.vicinity.core.presentation.ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.XSmall
import com.cjproductions.vicinity.core.presentation.ui.theme.VicinityTheme
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.CalendarAlt

@Composable
fun LabeledIcon(
  leadingIcon: ImageVector,
  textContent: @Composable RowScope.(Modifier) -> Unit,
  modifier: Modifier = Modifier,
  trailingIcon: (@Composable () -> Unit)? = null,
) {
  Row(
    horizontalArrangement = Arrangement.spacedBy(GlobalPaddingAndSize.Medium.dp),
    verticalAlignment = Alignment.CenterVertically,
    modifier = modifier,
  ) {
    Icon(
      imageVector = leadingIcon,
      contentDescription = null,
      modifier = Modifier.size(GlobalPaddingAndSize.Medium.dp),
    )
    textContent(Modifier.weight(1f))
    trailingIcon?.let { it() }
  }
}

@Composable
fun DefaultLabeledIcon(
  leadingIcon: ImageVector,
  text: String,
  textStyle: TextStyle = LocalTextStyle.current,
  containerModifier: Modifier = Modifier,
  trailingIcon: (@Composable () -> Unit)? = null,
) {
  LabeledIcon(
    leadingIcon = leadingIcon,
    textContent = { modifier ->
      Text(
        text = text,
        style = textStyle,
        modifier = modifier,
      )
    },
    trailingIcon = trailingIcon,
    modifier = containerModifier
  )
}

@Preview
@Composable
fun LabeledIconPreview() {
  VicinityTheme {
    Column(
      verticalArrangement = Arrangement.spacedBy(XSmall.dp)
    ) {
      DefaultLabeledIcon(
        leadingIcon = FontAwesomeIcons.Solid.CalendarAlt,
        text = "20-09-2025",
        trailingIcon = {
          Icon(
            imageVector = FontAwesomeIcons.Solid.CalendarAlt,
            contentDescription = null,
            modifier = Modifier.size(GlobalPaddingAndSize.Large.dp)
          )
        }
      )

      DefaultLabeledIcon(
        leadingIcon = FontAwesomeIcons.Solid.CalendarAlt,
        text = "Very long text that will wrap around and be truncated",
        trailingIcon = {
          Icon(
            imageVector = FontAwesomeIcons.Solid.CalendarAlt,
            contentDescription = null,
            modifier = Modifier.size(GlobalPaddingAndSize.Large.dp)
          )
        }
      )
    }
  }
}