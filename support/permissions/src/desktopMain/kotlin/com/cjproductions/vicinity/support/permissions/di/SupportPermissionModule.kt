package com.cjproductions.vicinity.support.permissions.di

import com.cjproductions.vicinity.support.permissions.SupportPermissionController
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val supportPermissionModule = module {
  singleOf(::SupportPermissionController)
}