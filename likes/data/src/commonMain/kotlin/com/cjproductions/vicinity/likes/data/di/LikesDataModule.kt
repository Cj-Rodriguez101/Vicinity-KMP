package com.cjproductions.vicinity.likes.data.di

import com.cjproductions.vicinity.likes.data.DefaultLikesRepository
import com.cjproductions.vicinity.likes.data.local.DefaultLocalLikesDataSource
import com.cjproductions.vicinity.likes.data.local.LocalLikesDataSource
import com.cjproductions.vicinity.likes.data.remote.DefaultRemoteLikesDataSource
import com.cjproductions.vicinity.likes.data.remote.RemoteLikesDataSource
import com.cjproductions.vicinity.likes.domain.LikesRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val likesDataModule = module {
  singleOf(::DefaultRemoteLikesDataSource).bind<RemoteLikesDataSource>()
  singleOf(::DefaultLocalLikesDataSource).bind<LocalLikesDataSource>()
  singleOf(::DefaultLikesRepository).bind<LikesRepository>()
}