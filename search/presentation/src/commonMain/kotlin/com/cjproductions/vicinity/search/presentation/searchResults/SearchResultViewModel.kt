@file:OptIn(ExperimentalCoroutinesApi::class)

package com.cjproductions.vicinity.search.presentation.searchResults

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.cjproductions.vicinity.auth.domain.ObserveLoggedInStateUseCase
import com.cjproductions.vicinity.core.domain.util.onSuccess
import com.cjproductions.vicinity.core.presentation.designsystem.components.UIText
import com.cjproductions.vicinity.core.presentation.ui.SnackBarController
import com.cjproductions.vicinity.core.presentation.ui.SnackBarEvent.SnackBarUiEvent
import com.cjproductions.vicinity.discover.domain.DiscoverRepository
import com.cjproductions.vicinity.likes.domain.GetEventLikeStateUseCase
import com.cjproductions.vicinity.likes.domain.ObserveLikesCountUseCase
import com.cjproductions.vicinity.likes.domain.ToggleLikeUseCase
import com.cjproductions.vicinity.location.domain.LocationRepository
import com.cjproductions.vicinity.search.presentation.searchResults.SearchResultAction.OnBackClick
import com.cjproductions.vicinity.search.presentation.searchResults.SearchResultAction.OnFilterClick
import com.cjproductions.vicinity.search.presentation.searchResults.SearchResultAction.OnSearchClick
import com.cjproductions.vicinity.search.presentation.searchResults.SearchResultAction.OnShareClick
import com.cjproductions.vicinity.search.presentation.searchResults.SearchResultAction.OnToggleLikeClick
import com.cjproductions.vicinity.search.presentation.searchResults.SearchResultDestination.Back
import com.cjproductions.vicinity.search.presentation.searchResults.SearchResultDestination.Filter
import com.cjproductions.vicinity.search.presentation.searchResults.SearchResultDestination.Search
import com.cjproductions.vicinity.search.presentation.searchResults.components.toSearchResultUi
import com.cjproductions.vicinity.support.haptics.presentation.PerformClickHapticsUseCase
import com.cjproductions.vicinity.support.search.SearchFilterParamsRepository
import com.cjproductions.vicinity.support.search.toFilterCount
import com.cjproductions.vicinity.support.share.presentation.ShareLinkUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import vicinity.core.presentation.ui.generated.resources.Res
import vicinity.core.presentation.ui.generated.resources.added_to_liked_events
import vicinity.core.presentation.ui.generated.resources.removed_from_liked_events

class SearchResultViewModel(
  private val discoverRepository: DiscoverRepository,
  private val getEventLikeState: GetEventLikeStateUseCase,
  private val toggleLikeUseCase: ToggleLikeUseCase,
  private val shareLinkUseCase: ShareLinkUseCase,
  private val performClickHaptics: PerformClickHapticsUseCase,
  private val snackBarController: SnackBarController,
  searchFilterParamsRepository: SearchFilterParamsRepository,
  locationRepository: LocationRepository,
  observeLoggedInStateUseCase: ObserveLoggedInStateUseCase,
  observeLikes: ObserveLikesCountUseCase,
): ViewModel() {

  private val _destination = Channel<SearchResultDestination>()
  val destination = _destination.receiveAsFlow()

  val uiState = combine(
    searchFilterParamsRepository.searchParams,
    searchFilterParamsRepository.filterParams,
  ) { searchParams, filterParams ->
    SearchResultViewState(
      title = searchParams?.keyword,
      location = searchParams?.location,
      filterCount = filterParams.toFilterCount(),
    )
  }.mapLatest { it }.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = SearchResultViewState()
  )

  private val eventPagingData = combine(
    searchFilterParamsRepository.searchParams,
    searchFilterParamsRepository.filterParams,
    locationRepository.location,
  ) { searchParams, filterParams, location ->
    discoverRepository.getEventFeed(
      searchParams = searchParams,
      filterParams = filterParams,
      location = location,
    ).cachedIn(viewModelScope)
  }.flatMapLatest { it }.cachedIn(viewModelScope)

  val pagedEvents =
    combine(
      flow = observeLoggedInStateUseCase.isLoggedIn,
      flow2 = eventPagingData,
      flow3 = observeLikes.likesCount,
    ) { isLoggedIn, pagingData, likes ->
      pagingData.map { event ->
        event.toSearchResultUi(
          savedState = getEventLikeState.invoke(event.normalizedTitle)
        )
      }
    }

  fun onAction(action: SearchResultAction) {
    when (action) {
      OnBackClick -> goToDestination(Back)
      OnSearchClick -> goToDestination(Search)
      OnFilterClick -> goToDestination(Filter)
      is OnShareClick -> shareLink(action.title)
      is OnToggleLikeClick -> toggleLikeAndTriggerHaptics(action)
      else -> Unit
    }
  }

  private fun toggleLikeAndTriggerHaptics(action: OnToggleLikeClick) {
    viewModelScope.launch {
      performClickHaptics.invoke()
      with(action) {
        toggleLikeUseCase.invoke(
          normalizedTitle = normalizedTitle,
          title = title,
          category = category,
          isLiked = isLiked,
          startDateTime = earliestTimeStamp,
          endDateTime = latestTimeStamp,
          image = image,
        ).onSuccess {
          snackBarController.sendEvent(
            SnackBarUiEvent(
              UIText.StringResourceId(Res.string.added_to_liked_events)
                .takeUnless { action.isLiked }
                ?: UIText.StringResourceId(Res.string.removed_from_liked_events)
            )
          )
        }
      }
    }
  }

  private fun shareLink(title: String) {
    viewModelScope.launch {
      shareLinkUseCase.invoke(title)
    }
  }

  private fun goToDestination(event: SearchResultDestination) {
    viewModelScope.launch { _destination.send(event) }
  }
}