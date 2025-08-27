package com.cjproductions.vicinity.support.share.presentation.di

import com.cjproductions.vicinity.support.share.domain.LinkShareCopyHandler
import com.cjproductions.vicinity.support.share.presentation.PlatformShareCopyHandler
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformShareModule: Module = module {
  singleOf(::PlatformShareCopyHandler).bind<LinkShareCopyHandler>()
}