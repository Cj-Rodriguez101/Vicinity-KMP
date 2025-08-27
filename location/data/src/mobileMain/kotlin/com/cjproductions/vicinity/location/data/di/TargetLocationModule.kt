package com.cjproductions.vicinity.location.data.di

import com.cjproductions.vicinity.location.data.PlatformLocator
import com.cjproductions.vicinity.location.domain.DomainLocationTracker
import dev.jordond.compass.geocoder.Geocoder
import dev.jordond.compass.geolocation.Geolocator
import dev.jordond.compass.geolocation.Locator
import dev.jordond.compass.geolocation.mobile.mobile
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val targetLocationModule = module {
  single<Geolocator> { Geolocator(Locator.mobile()) }
  single<Geocoder> { Geocoder() }
  singleOf(::PlatformLocator).bind<DomainLocationTracker>()
}