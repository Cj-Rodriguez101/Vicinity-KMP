@file:OptIn(ExperimentalCoroutinesApi::class)

package com.cjproductions.vicinity.eventDetail.presentation.eventDates

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cjproductions.vicinity.core.domain.util.onFailure
import com.cjproductions.vicinity.core.domain.util.onSuccess
import com.cjproductions.vicinity.eventDetail.domain.EventDetailRepository
import com.cjproductions.vicinity.eventDetail.presentation.eventDates.EventDateAction.OnRetryClicked
import com.cjproductions.vicinity.eventDetail.presentation.eventDates.EventDateState.Loaded
import com.cjproductions.vicinity.support.tools.toLocalDateTime
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EventDateViewModel(
    private val eventDetailRepository: EventDetailRepository,
    savedStateHandle: SavedStateHandle,
): ViewModel() {
    private val eventIds = savedStateHandle.get<Array<String>>(EVENT_IDS_KEY)
    private val date = savedStateHandle.get<Double>(DATE_KEY)
    private val retryCount = MutableStateFlow(0)
    val uiState: StateFlow<EventDateState> = retryCount.flatMapLatest { _ ->
        flow {
            if (eventIds.isNullOrEmpty() || date == null) {
                emit(Loaded())
            } else {
                eventDetailRepository.getEventsBasedOnIdsAndDate(
                    ids = eventIds.toList(),
                    date = date,
                ).onSuccess { eventDate ->
                    emit(
                        Loaded(
                            headerDate = date.toLocalDateTime(),
                            eventDates = eventDate.toEventDateUIs()
                        )
                    )
                }.onFailure {
                    emit(EventDateState.Error)
                }
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = EventDateState.Loading
    )

    fun onAction(action: EventDateAction) {
        when (action) {
            OnRetryClicked -> viewModelScope.launch { retryCount.emit(retryCount.value.inc()) }
        }
    }

    companion object {
        private const val EVENT_IDS_KEY = "eventIds"
        private const val DATE_KEY = "date"
    }
}