package com.cjproductions.vicinity.core.presentation.ui.error

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize
import org.jetbrains.compose.resources.stringResource
import vicinity.core.presentation.ui.generated.resources.Res.string
import vicinity.core.presentation.ui.generated.resources.error_generic
import vicinity.core.presentation.ui.generated.resources.retry

@Composable
fun ErrorCard(
  modifier: Modifier = Modifier,
  onRetry: () -> Unit,
) {
  Column(
    verticalArrangement = Arrangement.spacedBy(GlobalPaddingAndSize.Medium.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = modifier,
  ) {
    Text(text = stringResource(string.error_generic))
    Button(onClick = onRetry) {
      Text(text = stringResource(string.retry))
    }
  }
}