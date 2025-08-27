@file:OptIn(ExperimentalKoalaPlotApi::class)

package com.cjproductions.vicinity.likes.presentation.trending

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.Medium
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.XSmall
import com.cjproductions.vicinity.core.presentation.ui.components.TopNavBarWithBack
import com.cjproductions.vicinity.core.presentation.ui.error.ErrorCard
import com.cjproductions.vicinity.core.presentation.ui.theme.VicinityTheme
import com.cjproductions.vicinity.likes.presentation.trending.TrendingViewState.Error
import com.cjproductions.vicinity.likes.presentation.trending.TrendingViewState.Loaded
import com.cjproductions.vicinity.likes.presentation.trending.TrendingViewState.Loading
import io.github.koalaplot.core.bar.BulletGraphs
import io.github.koalaplot.core.pie.PieChart
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi
import io.github.koalaplot.core.xygraph.IntLinearAxisModel
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import vicinity.likes.presentation.generated.resources.Res
import vicinity.likes.presentation.generated.resources.categories
import vicinity.likes.presentation.generated.resources.highest_rated_events
import vicinity.likes.presentation.generated.resources.no_data_available
import vicinity.likes.presentation.generated.resources.popular_event_categories
import vicinity.likes.presentation.generated.resources.trending

@Composable
fun TrendingScreenRoot(
    viewModel: TrendingViewModel = koinViewModel(),
    onBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    TrendingScreen(
        uiState = uiState,
        onBackClick = onBack,
        onRetryClick = viewModel::retry,
    )
}

@Composable
private fun TrendingScreen(
    uiState: TrendingViewState,
    onRetryClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    VicinityTheme {
        Scaffold(
            topBar = {
                TopNavBarWithBack(
                    title = stringResource(Res.string.trending),
                    onBackClick = onBackClick
                )
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(
                        start = Medium.dp,
                        end = Medium.dp,
                        bottom = Medium.dp,
                    )
            ) {
                when (uiState) {
                    Error -> ErrorCard(
                        onRetry = onRetryClick,
                        modifier = Modifier.align(Alignment.Center),
                    )

                    is Loaded -> {
                        with(uiState.graphData) {
                            TrendingContent(
                                categories = categories,
                                titleCounts = titles,
                            )
                        }
                    }

                    Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}

@Composable
fun TrendingContent(
    categories: List<CategoryPercentage>,
    titleCounts: List<TitleCount>,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(Medium.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            if (categories.isNotEmpty()) {
                Text(
                    text = stringResource(Res.string.popular_event_categories),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = MaterialTheme.typography.titleMedium.fontWeight,
                    modifier = Modifier.padding(bottom = Medium.dp),
                )
                PieChart(
                    values = categories.map { it.percentage },
                    holeSize = 0.75F,
                    forceCenteredPie = true,
                    holeContent = {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = stringResource(Res.string.categories),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
                            )
                        }
                    },
                    label = { i ->
                        Text(
                            text = categories[i].category.toString(),
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Black,
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.surface,
                            shape = MaterialTheme.shapes.medium
                        ).padding(XSmall.dp)
                )
            } else Text(stringResource(Res.string.no_data_available))

        }

        item {
            if (titleCounts.isNotEmpty()) {
                Text(
                    text = stringResource(Res.string.highest_rated_events),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
                    modifier = Modifier.padding(vertical = XSmall.dp)
                )
                BulletGraphs(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surface,
                            shape = MaterialTheme.shapes.medium
                        )
                        .padding(horizontal = XSmall.dp),
                ) {
                    val maxRange = titleCounts.maxOf { it.count }
                    titleCounts.forEachIndexed { index, title ->
                        bullet(IntLinearAxisModel(0..maxRange)) {
                            label {
                                Text(
                                    text = title.title,
                                    textAlign = TextAlign.End,
                                    fontWeight = FontWeight.Black,
                                    maxLines = 2,
                                    style = MaterialTheme.typography.bodySmall,
                                )
                            }

                            if (index == titleCounts.lastIndex) {
                                axis { labels { Text("${it.toInt()}") } }
                            }

                            featuredMeasureBar(title.count)
                        }
                    }
                }
            } else Text(stringResource(Res.string.no_data_available))
        }
    }
}

@Preview
@Composable
private fun TrendingScreenPreview() {
    VicinityTheme {
        TrendingScreen(
            uiState = Loaded(
                graphData = GraphData(
                    categories = listOf(
                        CategoryPercentage(
                            category = "Category 1",
                            percentage = 0.5f,
                        ),
                        CategoryPercentage(
                            category = "Category 2",
                            percentage = 0.3f,
                        ),
                    ),
                    titles = listOf(
                        TitleCount(
                            title = "Title 1",
                            count = 10,
                        ),
                        TitleCount(
                            title = "Title 2",
                            count = 20,
                        ),
                    ),
                )
            ),
            onBackClick = {},
            onRetryClick = {},
        )
    }
}