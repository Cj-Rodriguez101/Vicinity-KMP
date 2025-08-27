package com.cjproductions.vicinity.discover.presentation.discover.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.Large
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.Medium
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.XXSmall
import com.valentinilk.shimmer.shimmer

@Composable
fun DiscoverLoadingCard(
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.shimmer().padding(horizontal = Medium.dp),
        elevation = 1.dp,
        shape = MaterialTheme.shapes.medium,
        backgroundColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.surface,
    ) {
        Box(
            modifier = Modifier.padding(Medium.dp)
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
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
                        Box(modifier = Modifier.fillMaxWidth().height(Large.dp))

                        Box(modifier = Modifier.fillMaxWidth().height(Medium.dp))
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun DiscoverCardLoadingPreview() {
    MaterialTheme {
        DiscoverLoadingCard()
    }
}