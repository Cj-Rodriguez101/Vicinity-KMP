package com.cjproductions.vicinity.support.tools.di

import com.cjproductions.vicinity.support.tools.AppDispatcher
import com.cjproductions.vicinity.support.tools.ApplicationScope
import com.cjproductions.vicinity.support.tools.Debouncer
import com.cjproductions.vicinity.support.tools.DefaultAppDispatcher
import kotlinx.coroutines.CoroutineScope
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val supportToolsModule = module {
  single<CoroutineScope> { ApplicationScope.scope }
  singleOf(::DefaultAppDispatcher).bind<AppDispatcher>()
  singleOf(::Debouncer)
}