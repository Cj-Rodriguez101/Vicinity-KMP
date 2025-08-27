package com.cjproductions.vicinity.support.share.presentation.di

import com.cjproductions.vicinity.support.share.presentation.ShareLinkUseCase
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

expect val platformShareModule: Module

val supportShareModule = module {
  includes(platformShareModule)
  singleOf(::ShareLinkUseCase)
}