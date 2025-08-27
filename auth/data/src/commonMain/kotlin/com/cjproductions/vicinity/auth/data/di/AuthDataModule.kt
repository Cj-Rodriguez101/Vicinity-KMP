package com.cjproductions.vicinity.auth.data.di

import com.cjproductions.vicinity.auth.data.AuthDataSource
import com.cjproductions.vicinity.auth.data.DefaultAuthDataSource
import com.cjproductions.vicinity.auth.data.DefaultAuthRepository
import com.cjproductions.vicinity.auth.data.validator.DefaultUserdataValidator
import com.cjproductions.vicinity.auth.domain.AuthRepository
import com.cjproductions.vicinity.auth.domain.validator.UserdataValidator
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authDataModule = module {
  singleOf(::DefaultUserdataValidator).bind<UserdataValidator>()
  singleOf(::DefaultAuthDataSource).bind<AuthDataSource>()
  singleOf(::DefaultAuthRepository).bind<AuthRepository>()
}