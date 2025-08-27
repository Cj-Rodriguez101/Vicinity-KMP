package com.cjproductions.vicinity.app.di

import com.cjproductions.vicinity.app.presentation.MainViewModel
import com.cjproductions.vicinity.auth.data.di.authDataModule
import com.cjproductions.vicinity.auth.presentation.di.authPresentationModule
import com.cjproductions.vicinity.core.data.database.di.coreDatabaseModule
import com.cjproductions.vicinity.core.data.preference.di.corePreferenceModule
import com.cjproductions.vicinity.core.data.serverless.di.coreServerlessModule
import com.cjproductions.vicinity.core.presentation.ui.di.corePresentationModule
import com.cjproductions.vicinity.discover.data.di.discoverDataModule
import com.cjproductions.vicinity.discover.presentation.di.discoverPresentationModule
import com.cjproductions.vicinity.eventDetail.data.di.eventDetailDataModule
import com.cjproductions.vicinity.eventDetail.presentation.di.eventDetailPresentationModule
import com.cjproductions.vicinity.geoEvents.presentation.di.geoEventsPresentationModule
import com.cjproductions.vicinity.likes.data.di.likesDataModule
import com.cjproductions.vicinity.likes.domain.likesDomainModule
import com.cjproductions.vicinity.likes.presentation.di.likesPresentationModule
import com.cjproductions.vicinity.location.data.di.locationDataModule
import com.cjproductions.vicinity.location.presentation.di.locationPresentationModule
import com.cjproductions.vicinity.profile.data.di.profileDataModule
import com.cjproductions.vicinity.profile.presentation.profile.di.profilePresentationModule
import com.cjproductions.vicinity.search.data.di.searchDataModule
import com.cjproductions.vicinity.search.presentation.di.searchPresentationModule
import com.cjproductions.vicinity.support.haptics.presentation.di.supportHapticsModule
import com.cjproductions.vicinity.support.permissions.di.supportPermissionModule
import com.cjproductions.vicinity.support.search.supportSearchParamModule
import com.cjproductions.vicinity.support.share.presentation.di.supportShareModule
import com.cjproductions.vicinity.support.tools.di.supportToolsModule
import com.cjproductions.vicinity.support.user.supportUserModule
import com.vicinity.core.data.connectivity.di.coreConnectivityModule
import com.vicinity.core.data.network.di.coreNetworkModule
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
  viewModelOf(::MainViewModel)
  includes(
    authDataModule,
    authPresentationModule,
    likesDomainModule,
    discoverDataModule,
    discoverPresentationModule,
    coreDatabaseModule,
    corePreferenceModule,
    coreServerlessModule,
    coreNetworkModule,
    coreConnectivityModule,
    corePresentationModule,
    locationDataModule,
    likesDataModule,
    likesPresentationModule,
    locationPresentationModule,
    geoEventsPresentationModule,
    profilePresentationModule,
    profileDataModule,
    eventDetailDataModule,
    eventDetailPresentationModule,
    searchDataModule,
    searchPresentationModule,
    supportToolsModule,
    supportSearchParamModule,
    supportPermissionModule,
    supportHapticsModule,
    supportUserModule,
    supportShareModule,
  )
}

fun initializeKoin(config: (KoinApplication.() -> Unit)? = null) {
  startKoin {
    config?.invoke(this)
    modules(appModule)
  }
}