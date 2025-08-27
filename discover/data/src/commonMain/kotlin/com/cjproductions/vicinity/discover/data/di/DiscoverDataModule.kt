package com.cjproductions.vicinity.discover.data.di

import com.cjproductions.vicinity.discover.data.DefaultDiscoverRepository
import com.cjproductions.vicinity.discover.data.local.DefaultLocalDiscoverDataSource
import com.cjproductions.vicinity.discover.data.local.LocalDiscoverDataSource
import com.cjproductions.vicinity.discover.data.paging.events.EventsRemoteMediator
import com.cjproductions.vicinity.discover.data.remote.DefaultRemoteDiscoverDataSource
import com.cjproductions.vicinity.discover.data.remote.RemoteDiscoverDataSource
import com.cjproductions.vicinity.discover.domain.DiscoverRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val discoverDataModule = module {
  singleOf(::DefaultRemoteDiscoverDataSource).bind<RemoteDiscoverDataSource>()
  singleOf(::DefaultLocalDiscoverDataSource).bind<LocalDiscoverDataSource>()
  singleOf(::DefaultDiscoverRepository).bind<DiscoverRepository>()
  singleOf(::EventsRemoteMediator)
}