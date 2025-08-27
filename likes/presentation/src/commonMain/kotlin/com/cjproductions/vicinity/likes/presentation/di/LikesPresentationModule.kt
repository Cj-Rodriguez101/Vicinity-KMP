package com.cjproductions.vicinity.likes.presentation.di

import com.cjproductions.vicinity.likes.presentation.likes.LikesViewModel
import com.cjproductions.vicinity.likes.presentation.likes.ToggleLikePerformHapticsUseCase
import com.cjproductions.vicinity.likes.presentation.trending.TrendingViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val likesPresentationModule = module {
  viewModelOf(::LikesViewModel)
  viewModelOf(::TrendingViewModel)
  singleOf(::ToggleLikePerformHapticsUseCase)
}