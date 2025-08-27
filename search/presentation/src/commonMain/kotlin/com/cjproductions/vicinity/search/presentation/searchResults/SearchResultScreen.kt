package com.cjproductions.vicinity.search.presentation.searchResults

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize
import com.cjproductions.vicinity.core.presentation.ui.components.RadiusVerticalGrid
import com.cjproductions.vicinity.core.presentation.ui.components.ScaffoldTopBar
import com.cjproductions.vicinity.core.presentation.ui.components.SearchFilterBar
import com.cjproductions.vicinity.core.presentation.ui.error.ErrorCard
import com.cjproductions.vicinity.core.presentation.ui.observeAsEvents
import com.cjproductions.vicinity.core.presentation.ui.theme.VicinityTheme
import com.cjproductions.vicinity.search.presentation.searchResults.SearchResultAction.OnBackClick
import com.cjproductions.vicinity.search.presentation.searchResults.SearchResultAction.OnEventClick
import com.cjproductions.vicinity.search.presentation.searchResults.SearchResultAction.OnFilterClick
import com.cjproductions.vicinity.search.presentation.searchResults.SearchResultAction.OnSearchClick
import com.cjproductions.vicinity.search.presentation.searchResults.SearchResultAction.OnShareClick
import com.cjproductions.vicinity.search.presentation.searchResults.SearchResultAction.OnToggleLikeClick
import com.cjproductions.vicinity.search.presentation.searchResults.SearchResultDestination.Back
import com.cjproductions.vicinity.search.presentation.searchResults.SearchResultDestination.Filter
import com.cjproductions.vicinity.search.presentation.searchResults.SearchResultDestination.Search
import com.cjproductions.vicinity.search.presentation.searchResults.components.SearchResultCard
import com.cjproductions.vicinity.search.presentation.searchResults.components.SearchResultUI
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import vicinity.core.presentation.ui.generated.resources.Res
import vicinity.core.presentation.ui.generated.resources.no_events
import vicinity.search.presentation.generated.resources.find_things
import vicinity.search.presentation.generated.resources.`in`
import vicinity.search.presentation.generated.resources.search
import vicinity.search.presentation.generated.resources.Res as SearchRes

@Composable
fun SearchResultScreenRoot(
  viewModel: SearchResultViewModel = koinViewModel(),
  onBackClick: () -> Unit,
  onGoToSearch: () -> Unit,
  onFilterClick: () -> Unit,
  onEventDetail: (String) -> Unit,
) {
  viewModel.destination.observeAsEvents { events ->
    when (events) {
      Back -> onBackClick()
      Search -> onGoToSearch()
      Filter -> onFilterClick()
    }
  }

  val pagedSearchResults = viewModel.pagedEvents.collectAsLazyPagingItems()
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  SearchResultScreen(
    uiState = uiState,
    pagedSearchResults = pagedSearchResults,
    onAction = { action ->
      when (action) {
        is OnEventClick -> {
          onEventDetail(action.title)
        }

        else -> viewModel.onAction(action)
      }
    }
  )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchResultScreen(
  uiState: SearchResultViewState,
  pagedSearchResults: LazyPagingItems<SearchResultUI>,
  onAction: (SearchResultAction) -> Unit,
) {
  BackHandler(
    enabled = true,
    onBack = { onAction(OnBackClick) }
  )
  VicinityTheme {
    Scaffold(
      contentWindowInsets = WindowInsets.safeDrawing,
      topBar = {
        ScaffoldTopBar(
          title = stringResource(SearchRes.string.search),
          onBackClick = { onAction(OnBackClick) },
          bottomContent = {
            SearchFilterBar(
              filterCount = uiState.filterCount,
              textContent = {
                Text(
                  text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                      append(
                        uiState.title
                          ?: stringResource(SearchRes.string.find_things)
                      )
                    }

                    uiState.location?.let {
                      append("\n")
                      append("${stringResource(SearchRes.string.`in`)} ")
                      append(uiState.location)
                    }
                  }
                )
              },
              onClick = { onAction(OnSearchClick) },
              onFilterClick = { onAction(OnFilterClick) },
            )
          },
        )
      },
    ) { padding ->
      Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
          .fillMaxSize()
          .padding(padding),
      ) {
        when {
          pagedSearchResults.loadState.refresh is LoadState.Loading && pagedSearchResults.itemCount == 0 -> {
            CircularProgressIndicator()
          }

          pagedSearchResults.loadState.refresh is LoadState.Error && pagedSearchResults.itemCount == 0 -> {
            ErrorCard(onRetry = { pagedSearchResults.retry() })
          }

          pagedSearchResults.loadState.refresh is LoadState.NotLoading &&
              pagedSearchResults.itemCount == 0 &&
              pagedSearchResults.loadState.source.refresh is LoadState.NotLoading -> {
            Text(text = stringResource(Res.string.no_events))
          }

          else -> {
            RadiusVerticalGrid(
              contentPadding = PaddingValues(vertical = GlobalPaddingAndSize.Medium.dp),
              modifier = Modifier.weight(1f)
            ) {
              items(pagedSearchResults.itemCount) { index ->
                pagedSearchResults[index]?.let { searchResult ->
                  SearchResultCard(
                    searchResult = searchResult,
                    onLikeClick = { isLiked ->
                      with(searchResult) {
                        onAction(
                          OnToggleLikeClick(
                            normalizedTitle = normalizedTitle,
                            title = title,
                            category = classification?.name.orEmpty(),
                            isLiked = isLiked,
                            image = image,
                            earliestTimeStamp = startTimeStamp,
                            latestTimeStamp = endTimeStamp,
                          )
                        )
                      }
                    },
                    onItemClick = { onAction(OnEventClick(it)) },
                    onShareClick = { onAction(OnShareClick(it)) },
                  )
                }
              }
            }
          }
        }
      }
    }
  }
}