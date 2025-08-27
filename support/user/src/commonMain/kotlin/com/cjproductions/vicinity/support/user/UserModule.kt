package com.cjproductions.vicinity.support.user

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val supportUserModule = module {
  singleOf(::DefaultUserSettingsDataSource).bind(UserSettingsDataSource::class)
  singleOf(::DefaultUserRepository).bind(UserRepository::class)
}