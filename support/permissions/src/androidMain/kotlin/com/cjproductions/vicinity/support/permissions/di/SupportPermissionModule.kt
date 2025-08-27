package com.cjproductions.vicinity.support.permissions.di

import com.cjproductions.vicinity.support.permissions.SupportPermissionController
import dev.icerock.moko.permissions.PermissionsController
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val supportPermissionModule = module {
  single<PermissionsController> { PermissionsController.invoke(get()) }
  singleOf(::SupportPermissionController)
}