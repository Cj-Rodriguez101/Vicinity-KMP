package com.cjproductions.vicinity.location.presentation.di

import com.cjproductions.vicinity.location.presentation.MapController
import com.cjproductions.vicinity.location.presentation.MapTileProvider
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ovh.plrapps.mapcompose.core.TileStreamProvider

actual val targetTileProviderModule: Module = module {
  singleOf(::MapTileProvider).bind<TileStreamProvider>()
  singleOf(::MapController)
}