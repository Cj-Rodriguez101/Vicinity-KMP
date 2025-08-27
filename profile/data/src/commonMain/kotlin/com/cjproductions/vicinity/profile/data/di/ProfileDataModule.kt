package com.cjproductions.vicinity.profile.data.di

import com.cjproductions.vicinity.profile.data.DefaultProfileImageDataSource
import com.cjproductions.vicinity.profile.data.DefaultProfileRepository
import com.cjproductions.vicinity.profile.data.ProfileImageDataSource
import com.cjproductions.vicinity.profile.data.user.DefaultProfileUserDataSource
import com.cjproductions.vicinity.profile.data.user.ProfileUserDataSource
import com.cjproductions.vicinity.profile.domain.ProfileRepository
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val profileStorageModule: Module

val profileDataModule = module {
  singleOf(::DefaultProfileUserDataSource).bind<ProfileUserDataSource>()
  singleOf(::DefaultProfileImageDataSource).bind<ProfileImageDataSource>()
  singleOf(::DefaultProfileRepository).bind<ProfileRepository>()
  includes(profileStorageModule)
}