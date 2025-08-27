package com.cjproductions.vicinity.likes.domain

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val likesDomainModule = module {
  singleOf(::ToggleLikeUseCase)
  singleOf(::ObserveLikesCountUseCase)
  singleOf(::GetEventLikeStateUseCase)
}