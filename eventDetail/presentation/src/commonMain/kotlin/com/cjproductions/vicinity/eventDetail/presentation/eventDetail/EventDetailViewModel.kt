@file:OptIn(ExperimentalCoroutinesApi::class)

package com.cjproductions.vicinity.eventDetail.presentation.eventDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cjproductions.vicinity.core.domain.util.DataError
import com.cjproductions.vicinity.core.domain.util.Result.Success
import com.cjproductions.vicinity.core.domain.util.onSuccess
import com.cjproductions.vicinity.core.presentation.designsystem.components.UIText
import com.cjproductions.vicinity.core.presentation.ui.SnackBarController
import com.cjproductions.vicinity.core.presentation.ui.SnackBarEvent.SnackBarUiEvent
import com.cjproductions.vicinity.eventDetail.domain.EventDetailRepository
import com.cjproductions.vicinity.eventDetail.presentation.eventDetail.EventDetailAction.OnChangeVenueClicked
import com.cjproductions.vicinity.eventDetail.presentation.eventDetail.EventDetailAction.OnDateClicked
import com.cjproductions.vicinity.eventDetail.presentation.eventDetail.EventDetailAction.OnRetryClicked
import com.cjproductions.vicinity.eventDetail.presentation.eventDetail.EventDetailAction.OnShareClicked
import com.cjproductions.vicinity.eventDetail.presentation.eventDetail.EventDetailAction.OnToggleLike
import com.cjproductions.vicinity.eventDetail.presentation.eventDetail.EventDetailAction.OnVenueClicked
import com.cjproductions.vicinity.eventDetail.presentation.eventDetail.EventDetailDestination.EventDates
import com.cjproductions.vicinity.eventDetail.presentation.eventDetail.EventDetailViewState.Error
import com.cjproductions.vicinity.eventDetail.presentation.eventDetail.EventDetailViewState.Loaded
import com.cjproductions.vicinity.eventDetail.presentation.eventDetail.EventDetailViewState.Loading
import com.cjproductions.vicinity.eventDetail.presentation.eventDetail.model.toEventUi
import com.cjproductions.vicinity.eventDetail.presentation.eventDetail.model.toVenueUis
import com.cjproductions.vicinity.likes.domain.GetEventLikeStateUseCase
import com.cjproductions.vicinity.likes.domain.LikeState
import com.cjproductions.vicinity.likes.domain.ObserveLikesCountUseCase
import com.cjproductions.vicinity.likes.domain.ToggleLikeUseCase
import com.cjproductions.vicinity.location.domain.LocationRepository
import com.cjproductions.vicinity.support.haptics.presentation.PerformClickHapticsUseCase
import com.cjproductions.vicinity.support.share.presentation.ShareLinkUseCase
import com.cjproductions.vicinity.support.tools.normalizeText
import com.cjproductions.vicinity.support.tools.toDouble
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import vicinity.core.presentation.ui.generated.resources.Res
import vicinity.core.presentation.ui.generated.resources.added_to_liked_events
import vicinity.core.presentation.ui.generated.resources.removed_from_liked_events
import kotlin.io.encoding.Base64

class EventDetailViewModel(
  private val eventDetailRepository: EventDetailRepository,
  private val locationRepository: LocationRepository,
  private val getEventLikeState: GetEventLikeStateUseCase,
  private val toggleLikeUseCase: ToggleLikeUseCase,
  private val shareLinkUseCase: ShareLinkUseCase,
  private val performClickHaptics: PerformClickHapticsUseCase,
  private val snackBarController: SnackBarController,
  observeLikes: ObserveLikesCountUseCase,
  savedStateHandle: SavedStateHandle,
): ViewModel() {

  private val retryCount = MutableStateFlow(0)
  private val deeplinkTitle = savedStateHandle.get<String>(ENCODED_TITLE_KEY)?.decodeTitle()
  private val title = deeplinkTitle ?: savedStateHandle.get<String>(TITLE_KEY).orEmpty()
  private val venueId: MutableStateFlow<String?> = MutableStateFlow(null)
  private var eventItems: List<EventScheduleUI>? = null
  private var category: String? = null

  private val _eventDetailDestination = Channel<EventDetailDestination>()
  val eventDetailDestination = _eventDetailDestination.receiveAsFlow()

  val uiState =
    combine(venueId, retryCount) { venueId, retryCount -> venueId }.transformLatest { venueId ->
      emit(Loading)
      val userLocation = locationRepository.location.value
      val eventDetail = eventDetailRepository.getEventsBasedOnKeyword(
        location = userLocation,
        keyword = title,
      )

      when (eventDetail) {
        is Success -> {
          with(eventDetail.data) {
            category = this.eventDetail.classifications.firstOrNull()?.name
            val selectedVenueId =
              venues.firstOrNull()?.id.takeIf { venues.size == 1 } ?: venueId
            eventItems = eventDetail.data.venueEventItems.values.flatMap {
              it.toEventItemUIs()
            }
            val loadedState = Loaded(
              venues = venues.toVenueUis(userLocation),
              selectedVenueId = selectedVenueId,
              selectedEvent = this.eventDetail.toEventUi(),
              events = selectedVenueId?.let { venueEventItems[it]?.toEventItemUIs() },
            )
            emit(loadedState)
          }
        }

        else -> emit(Error(DataError.Network.UNKNOWN))
      }
    }.stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = Loading,
    )

  val likeState = combine(
    observeLikes.likesCount,
    uiState
  ) { likeCount, state ->
    when (state) {
      is Loaded -> {
        getEventLikeState.invoke(title.normalizeText())
      }

      else -> LikeState.Hide
    }
  }.stateIn(
    scope = viewModelScope,
    started = SharingStarted.Lazily,
    initialValue = LikeState.Hide,
  )

  fun onViewAction(action: EventDetailAction) {
    when (action) {
      is OnVenueClicked -> updateVenue(action.venueId)

      OnRetryClicked -> updateRetryCount()

      is OnToggleLike -> toggleLikeAndTriggerHaptics(action)

      is OnDateClicked -> goToDate(action)

      OnChangeVenueClicked -> resetVenue()

      OnShareClicked -> shareLink()
    }
  }

  private fun shareLink() {
    viewModelScope.launch {
      (uiState.value as? Loaded)?.let {
        shareLinkUseCase.invoke(it.selectedEvent.title)
      }
    }
  }

  private fun resetVenue() {
    viewModelScope.launch { venueId.update { null } }
  }

  private fun goToDate(action: OnDateClicked) {
    viewModelScope.launch {
      _eventDetailDestination.send(
        EventDates(
          ids = action.eventIds,
          date = action.date,
        )
      )
    }
  }

  private fun toggleLikeAndTriggerHaptics(action: OnToggleLike) {
    viewModelScope.launch {
      performClickHaptics.invoke()
      toggleLikeUseCase.invoke(
        normalizedTitle = title.normalizeText(),
        title = deeplinkTitle ?: title,
        isLiked = action.isLiked,
        startDateTime = eventItems?.firstOrNull()?.dateTimes?.firstOrNull()?.toDouble(),
        endDateTime = eventItems?.lastOrNull()?.dateTimes?.lastOrNull()?.toDouble(),
        image = action.image,
        category = category.orEmpty(),
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

  private fun updateRetryCount() {
    viewModelScope.launch { retryCount.update { it.inc() } }
  }

  private fun updateVenue(venue: String) {
    (uiState.value as? Loaded)?.let { venueId.update { venue } }
  }

  private fun String.decodeTitle(): String {
    return try {
      Base64.UrlSafe.withPadding(Base64.PaddingOption.ABSENT).decode(this).decodeToString()
    } catch (e: Exception) {
      println(e)
      ""
    }
  }

  companion object {
    private const val TITLE_KEY = "title"
    const val ENCODED_TITLE_KEY = "encodedTitle"
  }
}