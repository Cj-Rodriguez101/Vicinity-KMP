package com.cjproductions.vicinity.core.presentation.ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.Medium
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.XSmall
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.XXLarge
import com.cjproductions.vicinity.core.presentation.ui.theme.LightGray
import com.cjproductions.vicinity.core.presentation.ui.theme.VicinityTheme
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Brands
import compose.icons.fontawesomeicons.brands.Google

@Composable
fun RadiusButton(
  label: String,
  enabled: Boolean = true,
  loading: Boolean = false,
  image: ImageVector? = null,
  colors: ButtonColors = ButtonDefaults.outlinedButtonColors(
    containerColor = MaterialTheme.colorScheme.primary,
    contentColor = MaterialTheme.colorScheme.surface,
    disabledContainerColor = LightGray,
    disabledContentColor = MaterialTheme.colorScheme.onSurface,
  ),
  border: BorderStroke? = null,
  modifier: Modifier = Modifier,
  onClick: () -> Unit,
) {
  OutlinedButton(
    onClick = onClick,
    enabled = enabled && !loading,
    colors = colors,
    border = border,
    shape = MaterialTheme.shapes.medium,
    modifier = modifier
      .fillMaxWidth()
      .height(XXLarge.dp)
  ) {
    Row(
      horizontalArrangement = Arrangement.spacedBy(
        space = XSmall.dp,
        alignment = Alignment.CenterHorizontally,
      )
    ) {
      if (loading) {
        CircularProgressIndicator(modifier = Modifier.size(Medium.dp))
      } else {
        image?.let {
          Icon(
            imageVector = image,
            contentDescription = label,
            modifier = Modifier.size(Medium.dp),
            tint = colors.contentColor.takeIf { enabled } ?: colors.disabledContentColor
          )
        }
        Text(
          text = label,
          color = colors.contentColor.takeIf { enabled } ?: colors.disabledContentColor,
        )
      }
    }
  }
}

@Composable
fun NegativeRadiusButton(
  label: String,
  loading: Boolean = false,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  RadiusButton(
    label = label,
    loading = loading,
    colors = ButtonDefaults.buttonColors(
      containerColor = MaterialTheme.colorScheme.error,
    ),
    modifier = modifier,
    onClick = onClick
  )
}

@Preview
@Composable
fun RadioButtonPreview() {
  VicinityTheme {
    Box(modifier = Modifier.padding(Medium.dp)) {
      RadiusButton(
        label = "Sign In",
        image = FontAwesomeIcons.Brands.Google
      ) { }
    }
  }
}