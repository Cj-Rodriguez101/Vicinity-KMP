package com.cjproductions.vicinity.support.search

import org.koin.dsl.module

val supportSearchParamModule = module {
  single<SearchFilterParamsRepository> { DefaultSearchFilterParamsRepository() }
}