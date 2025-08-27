@file:OptIn(ExperimentalFoundationApi::class, ExperimentalCoroutinesApi::class)

package com.cjproductions.vicinity.discover.presentation.discover

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.cjproductions.vicinity.auth.domain.ObserveLoggedInStateUseCase
import com.cjproductions.vicinity.core.domain.ClassificationName
import com.cjproductions.vicinity.core.domain.util.onSuccess
import com.cjproductions.vicinity.core.presentation.designsystem.components.UIText
import com.cjproductions.vicinity.core.presentation.ui.SnackBarController
import com.cjproductions.vicinity.core.presentation.ui.SnackBarEvent.SnackBarUiEvent
import com.cjproductions.vicinity.discover.domain.DiscoverRepository
import com.cjproductions.vicinity.discover.presentation.discover.DiscoverAction.OnEventClick
import com.cjproductions.vicinity.discover.presentation.discover.DiscoverAction.OnFilterClick
import com.cjproductions.vicinity.discover.presentation.discover.DiscoverAction.OnSearchClick
import com.cjproductions.vicinity.discover.presentation.discover.DiscoverAction.OnSelectClassification
import com.cjproductions.vicinity.discover.presentation.discover.DiscoverAction.OnShareClick
import com.cjproductions.vicinity.discover.presentation.discover.DiscoverAction.OnToggleEventLike
import com.cjproductions.vicinity.discover.presentation.discover.DiscoverDestination.DestinationDetail
import com.cjproductions.vicinity.discover.presentation.discover.DiscoverDestination.Filter
import com.cjproductions.vicinity.discover.presentation.discover.DiscoverDestination.SearchEvents
import com.cjproductions.vicinity.discover.presentation.discover.model.toDiscoverEventUi
import com.cjproductions.vicinity.likes.domain.GetEventLikeStateUseCase
import com.cjproductions.vicinity.likes.domain.ObserveLikesCountUseCase
import com.cjproductions.vicinity.likes.domain.ToggleLikeUseCase
import com.cjproductions.vicinity.location.domain.LocationRepository
import com.cjproductions.vicinity.support.haptics.presentation.PerformClickHapticsUseCase
import com.cjproductions.vicinity.support.search.DateType
import com.cjproductions.vicinity.support.search.FilterParams
import com.cjproductions.vicinity.support.search.SearchFilterParamsRepository
import com.cjproductions.vicinity.support.search.toFilterCount
import com.cjproductions.vicinity.support.share.presentation.ShareLinkUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import vicinity.core.presentation.ui.generated.resources.Res
import vicinity.core.presentation.ui.generated.resources.added_to_liked_events
import vicinity.core.presentation.ui.generated.resources.removed_from_liked_events

class DiscoverViewModel(
  private val locationRepository: LocationRepository,
  private val searchFilterParamsRepository: SearchFilterParamsRepository,
  private val toggleLikeUseCase: ToggleLikeUseCase,
  private val getEventLikeState: GetEventLikeStateUseCase,
  private val shareLinkUseCase: ShareLinkUseCase,
  private val performClickHaptics: PerformClickHapticsUseCase,
  private val snackBarController: SnackBarController,
  observeLikes: ObserveLikesCountUseCase,
  discoverRepository: DiscoverRepository,
  observeLoggedInStateUseCase: ObserveLoggedInStateUseCase,
): ViewModel() {

  private val location = locationRepository.location
  private val _uiState = MutableStateFlow(DiscoverViewState())
  val uiState =
    combine(
      searchFilterParamsRepository.filterParams,
      location,
      _uiState,
    ) { filterParams, location, uiState ->
      uiState.copy(
        filterCount = filterParams.toFilterCount(),
        selectedSegments = filterParams?.classificationNames?.toList()?.map { it.value },
        location = location,
        segments = discoverRepository.segments.map { it.toClassificationUI(filterParams?.classificationNames) }
      )
    }.stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = DiscoverViewState(location = location.value),
    )

  private val eventPagingData = combine(
    searchFilterParamsRepository.searchParams,
    searchFilterParamsRepository.filterParams,
    location,
  ) { searchParams, filterParams, location ->
    discoverRepository.getEventFeed(
      searchParams = searchParams,
      filterParams = filterParams,
      location = location,
    )
  }.flatMapLatest { it }.cachedIn(viewModelScope)

  private val likeCount = observeLikes.likesCount

  private val isLoggedIn = observeLoggedInStateUseCase.isLoggedIn

  val pagedEvents =
    combine(
      flow = isLoggedIn,
      flow2 = eventPagingData,
      flow3 = uiState,
      flow4 = likeCount,
    ) { isLoggedIn, pagingData, uiState, _ ->
      pagingData.map { event ->
        event.toDiscoverEventUi(
          likeState = getEventLikeState.invoke(event.normalizedTitle),
          userLocation = uiState.location,
        )
      }
    }.cachedIn(viewModelScope)

  private val _destination = Channel<DiscoverDestination>()
  val destination = _destination.receiveAsFlow()

  private val _pageRefreshEvents = MutableSharedFlow<Unit>(replay = 1)
  val pageRefreshEvents = _pageRefreshEvents.asSharedFlow()

  init {
    refreshUserLocation()
  }

  private fun refreshUserLocation() {
    viewModelScope.launch {
      locationRepository.getLocation().getOrNull()?.let { currentLocation ->
        if (location.value?.defaultLocation?.country == currentLocation.country) return@launch
        locationRepository.updateDefaultLocation(currentLocation)
      }
    }
  }

  fun onAction(event: DiscoverAction) {
    when (event) {
      is OnSelectClassification -> filterByClassification(event)

      is OnEventClick -> goToEventDetail(title = event.title)

      is OnSearchClick -> goToSearch()

      is OnFilterClick -> goToFilter()

      is OnToggleEventLike -> toggleLikeAndPerformhaptics(event)

      is OnShareClick -> shareLink(event.title)

      else -> Unit
    }
  }

  private fun filterByClassification(event: OnSelectClassification) {
    viewModelScope.launch {
      val selectedClassificationName =
        ClassificationName.fromValue(event.name) ?: return@launch
      val filterParams = searchFilterParamsRepository.filterParams.value
      val currentClassifications = filterParams?.classificationNames.orEmpty()
      val updatedClassifications =
        if (currentClassifications.contains(selectedClassificationName)) {
          currentClassifications.toMutableList().minus(selectedClassificationName)
        } else {
          currentClassifications.toMutableList().plus(selectedClassificationName)
        }
      val updatedFilterParams = filterParams?.copy(
        classificationNames = updatedClassifications.distinct()
      ) ?: FilterParams(
        classificationNames = listOf(selectedClassificationName),
        dateType = DateType.AnyDate,
        selectedDates = null,
        sortType = null,
      )
      searchFilterParamsRepository.updateFilterParams(updatedFilterParams)
    }
  }

  private fun goToSearch() {
    viewModelScope.launch { _destination.send(SearchEvents) }
  }

  private fun goToFilter() {
    viewModelScope.launch { _destination.send(Filter) }
  }

  private fun toggleLikeAndPerformhaptics(event: OnToggleEventLike) {
    viewModelScope.launch {
      performClickHaptics.invoke()
      with(event) {
        toggleLikeUseCase.invoke(
          normalizedTitle = normalizedTitle,
          title = title,
          isLiked = isLiked,
          startDateTime = startTimeStamp,
          endDateTime = endTimeStamp,
          image = image,
          category = category,
        ).onSuccess {
          snackBarController.sendEvent(
            SnackBarUiEvent(
              UIText.StringResourceId(Res.string.added_to_liked_events)
                .takeUnless { isLiked }
                ?: UIText.StringResourceId(Res.string.removed_from_liked_events)
            )
          )
        }
      }
    }
  }

  private fun shareLink(title: String) {
    viewModelScope.launch { shareLinkUseCase.invoke(title) }
  }

  fun goToEventDetail(title: String) {
    viewModelScope.launch { _destination.send(DestinationDetail(title)) }
  }
}