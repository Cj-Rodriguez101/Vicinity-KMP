package com.cjproductions.vicinity.search.data.di

import com.cjproductions.vicinity.search.data.DefaultSearchRepository
import com.cjproductions.vicinity.search.data.local.DefaultLocalSearchDataSource
import com.cjproductions.vicinity.search.data.local.LocalSearchDataSource
import com.cjproductions.vicinity.search.data.remote.DefaultRemoteSearchDataSource
import com.cjproductions.vicinity.search.data.remote.RemoteSearchDataSource
import com.cjproductions.vicinity.search.domain.SearchRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val searchDataModule = module {
  singleOf(::DefaultRemoteSearchDataSource).bind<RemoteSearchDataSource>()
  singleOf(::DefaultLocalSearchDataSource).bind<LocalSearchDataSource>()
  singleOf(::DefaultSearchRepository).bind<SearchRepository>()
}