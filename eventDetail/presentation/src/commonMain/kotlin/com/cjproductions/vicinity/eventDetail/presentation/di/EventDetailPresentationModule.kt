package com.cjproductions.vicinity.eventDetail.presentation.di

import com.cjproductions.vicinity.eventDetail.presentation.eventDates.EventDateViewModel
import com.cjproductions.vicinity.eventDetail.presentation.eventDetail.EventDetailViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val eventDetailPresentationModule = module {
  viewModelOf(::EventDetailViewModel)
  viewModelOf(::EventDateViewModel)
}