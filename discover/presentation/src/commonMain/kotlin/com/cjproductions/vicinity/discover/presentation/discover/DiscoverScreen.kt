package com.cjproductions.vicinity.discover.presentation.discover

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.window.core.layout.WindowWidthSizeClass
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import com.cjproductions.vicinity.core.presentation.designsystem.components.UIText
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize
import com.cjproductions.vicinity.core.presentation.ui.GlobalPaddingAndSize.XSmall
import com.cjproductions.vicinity.core.presentation.ui.SnackBarAction
import com.cjproductions.vicinity.core.presentation.ui.SnackBarController
import com.cjproductions.vicinity.core.presentation.ui.SnackBarEvent.DismissSnackBarEvent
import com.cjproductions.vicinity.core.presentation.ui.SnackBarEvent.SnackBarUiEvent
import com.cjproductions.vicinity.core.presentation.ui.components.RadiusVerticalGrid
import com.cjproductions.vicinity.core.presentation.ui.error.ErrorCard
import com.cjproductions.vicinity.core.presentation.ui.observeAsEvents
import com.cjproductions.vicinity.discover.presentation.discover.DiscoverAction.OnBackClick
import com.cjproductions.vicinity.discover.presentation.discover.DiscoverAction.OnEventClick
import com.cjproductions.vicinity.discover.presentation.discover.DiscoverAction.OnSelectClassification
import com.cjproductions.vicinity.discover.presentation.discover.DiscoverAction.OnShareClick
import com.cjproductions.vicinity.discover.presentation.discover.DiscoverAction.OnToggleEventLike
import com.cjproductions.vicinity.discover.presentation.discover.DiscoverDestination.DestinationDetail
import com.cjproductions.vicinity.discover.presentation.discover.DiscoverDestination.Filter
import com.cjproductions.vicinity.discover.presentation.discover.DiscoverDestination.RefreshEvents
import com.cjproductions.vicinity.discover.presentation.discover.DiscoverDestination.SearchEvents
import com.cjproductions.vicinity.discover.presentation.discover.components.CenteredBox
import com.cjproductions.vicinity.discover.presentation.discover.components.ClassificationCard
import com.cjproductions.vicinity.discover.presentation.discover.components.DiscoverCard
import com.cjproductions.vicinity.discover.presentation.discover.components.DiscoverLoadingGrid
import com.cjproductions.vicinity.discover.presentation.discover.components.TopNavWithSearchFilter
import com.cjproductions.vicinity.discover.presentation.discover.model.ClassificationUI
import com.cjproductions.vicinity.discover.presentation.discover.model.DiscoverEventUi
import com.cjproductions.vicinity.discover.presentation.discover.model.toLocationUi
import com.cjproductions.vicinity.likes.domain.isLiked
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import vicinity.core.presentation.ui.generated.resources.Res
import vicinity.core.presentation.ui.generated.resources.no_events
import vicinity.core.presentation.ui.generated.resources.retry
import vicinity.discover.presentation.generated.resources.search_events
import vicinity.discover.presentation.generated.resources.Res as DiscoverRes

@Composable
fun DiscoverScreenRoot(
  viewModel: DiscoverViewModel = koinViewModel(),
  onOpenCountrySelect: () -> Unit,
  onSearchClick: () -> Unit,
  onBackClick: () -> Unit,
  onFilterClick: () -> Unit,
  onEventDetail: (String) -> Unit,
) {
  val pagedEvents = viewModel.pagedEvents.collectAsLazyPagingItems()
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  viewModel.destination.observeAsEvents {
    when (it) {
      RefreshEvents -> {
        pagedEvents.refresh()
      }

      Filter -> onFilterClick()
      is DestinationDetail -> onEventDetail(it.title)
      is SearchEvents -> onSearchClick()
    }
  }
  LaunchedEffect(Unit) {
    viewModel.pageRefreshEvents.collectLatest {
      pagedEvents.refresh()
    }
  }

  DiscoverScreen(
    events = pagedEvents,
    segments = uiState.segments,
    uiState = uiState,
    onOpenCountrySelect = onOpenCountrySelect,
    onAction = { action ->
      when (action) {
        OnBackClick -> {
          onBackClick()
        }

        else -> Unit
      }
      viewModel.onAction(action)
    },
  )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun DiscoverScreen(
  events: LazyPagingItems<DiscoverEventUi>,
  segments: List<ClassificationUI>,
  onAction: (DiscoverAction) -> Unit,
  uiState: DiscoverViewState,
  onOpenCountrySelect: () -> Unit,
) {
  Scaffold(
    contentWindowInsets = WindowInsets.safeDrawing,
    topBar = {
      TopNavWithSearchFilter(
        filterCount = uiState.filterCount,
        query = stringResource(DiscoverRes.string.search_events),
        flagImage = uiState.location?.toLocationUi()?.image,
        onOpenCountrySelect = onOpenCountrySelect,
        onSearchClick = { onAction(DiscoverAction.OnSearchClick) },
        onFilterClick = { onAction(DiscoverAction.OnFilterClick) },
      )
    },
  ) { padding ->
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier
        .fillMaxSize()
        .padding(padding),
    ) {
      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(
          space = GlobalPaddingAndSize.Medium.dp,
          alignment = Alignment.CenterHorizontally
            .takeIf {
              currentWindowAdaptiveInfo()
                .windowSizeClass.windowWidthSizeClass != WindowWidthSizeClass.COMPACT
            } ?: Alignment.Start,
        ),
        verticalAlignment = Alignment.CenterVertically,
        contentPadding = PaddingValues(
          horizontal = GlobalPaddingAndSize.Medium.dp,
          vertical = XSmall.dp,
        ),
        modifier = Modifier
      ) {
        items(items = segments) { segment ->
          ClassificationCard(
            classification = segment,
            onClick = { onAction(OnSelectClassification(segment.name)) },
          )
        }
      }

      val snackBarController: SnackBarController = koinInject()

      LaunchedEffect(key1 = events.loadState) {
        if (events.loadState.refresh is LoadState.Error) {
          snackBarController.sendEvent(
            SnackBarUiEvent(
              message = UIText.DynamicString((events.loadState.refresh as LoadState.Error).error.message.toString()),
              duration = SnackbarDuration.Indefinite,
              action = SnackBarAction(
                name = getString(Res.string.retry),
                action = {
                  events.refresh()
                }
              )
            )
          )
        }
      }

      when {
        events.loadState.refresh is LoadState.Loading && events.itemCount == 0 -> {
          DiscoverLoadingGrid(modifier = Modifier.weight(1f))
        }

        events.loadState.refresh is LoadState.Error && events.itemCount == 0 -> {
          CenteredBox { ErrorCard(onRetry = { events.retry() }) }
        }

        events.loadState.refresh is LoadState.NotLoading &&
            events.itemCount == 0 &&
            events.loadState.source.refresh is LoadState.NotLoading -> {
          CenteredBox { Text(text = stringResource(Res.string.no_events)) }
        }

        else -> {
          LaunchedEffect(Unit) { snackBarController.sendEvent(DismissSnackBarEvent) }
          RadiusVerticalGrid(modifier = Modifier.weight(1f)) {
            items(events.itemCount) {
              events[it]?.let { event ->
                DiscoverCard(
                  event = event,
                  onEventClick = { onAction(OnEventClick(title = event.title)) },
                  onShareClick = { onAction(OnShareClick(title = event.title)) },
                  onLikeClick = {
                    onAction(
                      OnToggleEventLike(
                        normalizedTitle = event.normalizedTitle,
                        title = event.title,
                        category = event.classification?.name.orEmpty(),
                        isLiked = event.likeState.isLiked(),
                        image = event.image,
                        startTimeStamp = event.startTimestamp,
                        endTimeStamp = event.endTimestamp,
                      )
                    )
                  }
                )
              }
            }

            when (events.loadState.append) {
              is LoadState.Loading -> {
                item {
                  Box(
                    modifier = Modifier.height(100.dp),
                    contentAlignment = Alignment.Center,
                  ) {
                    CircularProgressIndicator()
                  }
                }
              }

              is LoadState.Error -> {
                item {
                  ErrorCard(onRetry = events::retry)
                }
              }

              else -> Unit
            }
          }
        }
      }
    }
  }
}