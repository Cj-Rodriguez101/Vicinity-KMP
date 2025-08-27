package com.cjproductions.vicinity.auth.presentation.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.Medium
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.XSmall
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.XXXLarge
import com.cjproductions.vicinity.core.presentation.ui.components.RadiusButton
import com.cjproductions.vicinity.core.presentation.ui.components.TopNavBarWithBack
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import vicinity.auth.presentation.generated.resources.`continue`
import vicinity.auth.presentation.generated.resources.Res as AuthRes

@Composable
fun OnboardingScreenRoot(
    onBackClicked: () -> Unit,
    onContinueClicked: () -> Unit,
) {
    OnboardingScreen(
        onBackClicked = onBackClicked,
        onContinueClicked = onContinueClicked,
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun OnboardingScreen(
    onBackClicked: () -> Unit,
    onContinueClicked: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
    ) {
        val pagerState = rememberPagerState(pageCount = { pageItems.size })
        val currentIndex by remember { derivedStateOf { pagerState.currentPage } }
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            HorizontalPager(
                state = pagerState,
                verticalAlignment = Alignment.Top,
                snapPosition = SnapPosition.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.83f)
            ) { index ->
                Column(
                    verticalArrangement = Arrangement.spacedBy(Medium.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Image(
                        painter = painterResource(pageItems[index].image),
                        contentScale = ContentScale.FillBounds,
                        contentDescription = null,
                        modifier = Modifier.fillMaxHeight(0.75f)
                    )

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(
                            space = Medium.dp,
                            alignment = Alignment.CenterVertically,
                        ),
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(horizontal = Medium.dp)
                    ) {
                        Text(
                            text = pageItems[index].title.asString(),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                        )

                        Text(
                            text = pageItems[index].description.asString(),
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }

            PagerIndicators(
                pageSize = pageItems.size,
                currentIndex = currentIndex,
            )

            AnimatedContent(
                targetState = currentIndex == pagerState.pageCount - 1,
                transitionSpec = {
                    fadeIn(animationSpec = tween(300)) togetherWith
                            fadeOut(animationSpec = tween(300))
                }
            ) { showButton ->
                Box(
                    modifier = Modifier
                        .height(XXXLarge.dp.takeIf { showButton } ?: 0.dp)
                        .fillMaxWidth()
                        .padding(horizontal = Medium.dp.takeIf { showButton } ?: 0.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (showButton) {
                        RadiusButton(
                            label = stringResource(AuthRes.string.`continue`),
                            onClick = onContinueClicked,
                        )
                    }
                }
            }
        }

        TopNavBarWithBack(onBackClick = onBackClicked)
    }
}

@Composable
fun PagerIndicators(
    pageSize: Int,
    currentIndex: Int,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(XSmall.dp)) {
        for (i in 0 until pageSize) {
            Surface(
                modifier = Modifier.size(XSmall.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary.takeIf { currentIndex == i }
                    ?: Color.LightGray
            ) {}
        }
    }
}

@Preview
@Composable
fun OnboardingScreenPreview() {
    OnboardingScreen(
        onBackClicked = {},
        onContinueClicked = {},
    )
}