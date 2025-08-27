package com.cjproductions.vicinity.profile.presentation.profile.profileDisplay.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize
import com.cjproductions.vicinity.core.presentation.ui.components.NegativeRadiusButton
import com.cjproductions.vicinity.core.presentation.ui.components.RadiusButton
import org.jetbrains.compose.resources.stringResource
import vicinity.profile.presentation.generated.resources.Res
import vicinity.profile.presentation.generated.resources.are_you_sure
import vicinity.profile.presentation.generated.resources.cancel
import vicinity.profile.presentation.generated.resources.delete
import vicinity.profile.presentation.generated.resources.delete_account

@Composable
internal fun ConfirmDeleteDialog(
  loading: Boolean,
  onDelete: () -> Unit,
  onDismiss: () -> Unit,
) {
  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text(
        text = stringResource(Res.string.delete_account),
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Black,
        fontSize = 16.sp,
      )
    },
    text = {
      Text(
        text = stringResource(Res.string.are_you_sure),
        style = MaterialTheme.typography.bodyMedium,
        fontSize = 16.sp,
      )
    },
    buttons = {
      Row(
        horizontalArrangement = Arrangement.spacedBy(GlobalPaddingAndSize.Medium.dp),
        modifier = Modifier.fillMaxWidth()
          .padding(
            start = GlobalPaddingAndSize.Medium.dp,
            end = GlobalPaddingAndSize.Medium.dp,
            bottom = GlobalPaddingAndSize.Medium.dp,
          )
      ) {
        RadiusButton(
          label = stringResource(Res.string.cancel),
          modifier = Modifier.weight(1f),
          onClick = onDismiss,
        )

        NegativeRadiusButton(
          label = stringResource(Res.string.delete),
          loading = loading,
          modifier = Modifier.weight(1f),
          onClick = {
            onDelete()
            onDismiss()
          },
        )
      }
    }
  )
}