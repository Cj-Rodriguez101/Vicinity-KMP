package com.cjproductions.vicinity.eventDetail.data.di

import com.cjproductions.vicinity.eventDetail.data.DefaultEventDetailRepository
import com.cjproductions.vicinity.eventDetail.data.remote.DefaultRemoteEventDetailDataSource
import com.cjproductions.vicinity.eventDetail.data.remote.RemoteEventDetailDataSource
import com.cjproductions.vicinity.eventDetail.domain.EventDetailRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val eventDetailDataModule = module {
  singleOf(::DefaultRemoteEventDetailDataSource).bind<RemoteEventDetailDataSource>()
  singleOf(::DefaultEventDetailRepository).bind<EventDetailRepository>()
}