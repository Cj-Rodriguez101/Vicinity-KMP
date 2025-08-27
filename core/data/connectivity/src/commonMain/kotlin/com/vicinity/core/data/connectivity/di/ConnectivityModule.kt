package com.vicinity.core.data.connectivity.di

import com.vicinity.core.data.connectivity.ConnectivityRepository
import com.vicinity.core.data.connectivity.DefaultConnectivityRepository
import com.vicinity.core.data.connectivity.PlatformConnectivityManager
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreConnectivityModule = module {
  singleOf(::PlatformConnectivityManager)
  singleOf(::DefaultConnectivityRepository).bind<ConnectivityRepository>()
}