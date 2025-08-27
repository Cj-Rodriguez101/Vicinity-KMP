package com.cjproductions.vicinity.likes.presentation.trending

import com.cjproductions.vicinity.likes.domain.model.Like

sealed class TrendingViewState {
    data object Loading: TrendingViewState()
    data class Loaded(val graphData: GraphData): TrendingViewState()
    data object Error: TrendingViewState()
}

data class GraphData(
    val categories: List<CategoryPercentage> = emptyList(),
    val titles: List<TitleCount> = emptyList(),
)

data class CategoryPercentage(
    val category: String,
    val percentage: Float,
)

data class TitleCount(
    val title: String,
    val count: Int,
)

private fun List<Like>.toCategoryPercentage(): List<CategoryPercentage> {
    return groupBy { it.category }.map { (category, likes) ->
        CategoryPercentage(
            category = category,
            percentage = likes.size.toFloat() / size,
        )
    }
}

private fun List<Like>.toTitleCount() = groupBy { it.title }.map { (title, likes) ->
    TitleCount(
        title = title,
        count = likes.size,
    )
}.sortedByDescending { it.count }.take(minOf(this.size, TOP_5_TITLE_COUNT))

private const val TOP_5_TITLE_COUNT = 5


fun List<Like>.toGraphData() = GraphData(
    categories = toCategoryPercentage(),
    titles = toTitleCount(),
)