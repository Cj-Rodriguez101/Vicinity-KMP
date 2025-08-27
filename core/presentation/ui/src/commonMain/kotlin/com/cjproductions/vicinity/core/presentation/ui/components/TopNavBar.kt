@file:OptIn(ExperimentalMaterial3Api::class)

package com.cjproductions.vicinity.core.presentation.ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.Medium
import dev.carlsen.flagkit.FlagIcons
import dev.carlsen.flagkit.flagicons.IO
import org.jetbrains.compose.resources.vectorResource
import vicinity.core.presentation.ui.generated.resources.Back
import vicinity.core.presentation.ui.generated.resources.Res

@Composable
fun TopNavBar(
    modifier: Modifier = Modifier,
    containerColor: Color = Color.Transparent,
    leadingContent: (@Composable () -> Unit)? = null,
    title: @Composable () -> Unit = {},
    trailingContent: (@Composable () -> Unit)? = null,
    bottomContent: (@Composable () -> Unit)? = null,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(GlobalPaddingAndSize.XSmall.dp),
        modifier = Modifier
            .drawBehind { drawRect(containerColor) }
            //.background(color = containerColor)
            .windowInsetsPadding(TopAppBarDefaults.windowInsets)
    ) {
        TopAppBar(
            title = title,
            actions = { trailingContent?.let { it() } },
            navigationIcon = { leadingContent?.let { it() } },
            windowInsets = WindowInsets(
                left = 0.dp,
                top = 0.dp,
                right = 0.dp,
                bottom = 0.dp,
            ),
            modifier = modifier.fillMaxWidth(),
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = containerColor,
                scrolledContainerColor = containerColor,
            ),
        )

        Column(
            modifier = Modifier.padding(horizontal = Medium.dp)
        ) {
            bottomContent?.let { it() }
        }
    }
}

@Composable
fun TopNavBarWithBack(
    title: String? = null,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    trailingContent: (@Composable () -> Unit)? = null,
    bottomContent: (@Composable () -> Unit)? = null,
) {
    ScaffoldTopBar(
        title = title,
        onBackClick = onBackClick,
        trailingContent = trailingContent,
        bottomContent = bottomContent,
        modifier = modifier,
    )
}

@Composable
fun ScaffoldTopBar(
    title: String? = null,
    modifier: Modifier = Modifier,
    onBackClick: (() -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    bottomContent: (@Composable () -> Unit)? = null,
) {
    TopNavBar(
        modifier = modifier,
        title = {
            title?.let {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Black,
                )
            }
        },
        trailingContent = trailingContent,
        bottomContent = bottomContent,
        leadingContent = {
            onBackClick?.let {
                RoundedButton(
                    icon = vectorResource(Res.drawable.Back),
                    onClick = onBackClick,
                )
            }
        }
    )
}

@Composable
fun AnimatedTopBar(
    titleBackgroundAlpha: Float,
    title: String? = null,
    modifier: Modifier = Modifier,
    onBackClick: (() -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    bottomContent: (@Composable () -> Unit)? = null,
) {
    TopNavBar(
        containerColor = MaterialTheme.colorScheme.background.copy(alpha = titleBackgroundAlpha),
        modifier = modifier,
        title = {
            title?.let {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = titleBackgroundAlpha),
                    fontWeight = FontWeight.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        },
        trailingContent = trailingContent,
        bottomContent = bottomContent,
        leadingContent = {
            onBackClick?.let {
                RoundedButton(
                    icon = vectorResource(Res.drawable.Back),
                    onClick = onBackClick,
                )
            }
        }
    )
}


@Preview
@Composable
fun TopNavBarPreview() {
    ScaffoldTopBar(
        title = "Vicinity",
        bottomContent = null,
        trailingContent = { FlagCircle(FlagIcons.IO) { } },
        modifier = Modifier,
    )
}

@Preview
@Composable
fun TopNavBarWithBackPreview() {
    TopNavBarWithBack(
        title = "Vicinity",
        onBackClick = {},
        trailingContent = null,
    )
}