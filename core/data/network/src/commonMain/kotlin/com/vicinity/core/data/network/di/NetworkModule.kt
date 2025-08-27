package com.vicinity.core.data.network.di

import com.vicinity.core.data.network.HttpClientFactory
import org.koin.dsl.module

val coreNetworkModule = module {
  single { HttpClientFactory().build() }
}