@file:OptIn(ExperimentalCoroutinesApi::class)

package com.cjproductions.vicinity.likes.presentation.trending

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cjproductions.vicinity.likes.domain.LikesRepository
import com.cjproductions.vicinity.likes.presentation.trending.TrendingViewState.Loaded
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TrendingViewModel(
    likesRepository: LikesRepository,
): ViewModel() {
    private val retryCount = MutableStateFlow(0)

    val uiState: StateFlow<TrendingViewState> = combine(
        flow = likesRepository.allLikes,
        flow2 = retryCount,
    ) { likes, retryCount -> likes to retryCount }.transformLatest { (likes, retryCount) ->
        emit(TrendingViewState.Loading)
        try {
            emit(Loaded(likes.toGraphData()))
        } catch (e: Exception) {
            emit(TrendingViewState.Error)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TrendingViewState.Loading,
    )

    fun retry() {
        viewModelScope.launch {
            retryCount.update { it.inc() }
        }
    }
}