package com.cjproductions.vicinity.core.presentation.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize

@Composable
fun RadiusCardContainer(
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    modifier: Modifier = Modifier.fillMaxWidth(),
    content: @Composable () -> Unit,
) {
    RadiusCard(
        backgroundColor = backgroundColor,
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier.padding(GlobalPaddingAndSize.Medium.dp)
        ) { content() }
    }
}