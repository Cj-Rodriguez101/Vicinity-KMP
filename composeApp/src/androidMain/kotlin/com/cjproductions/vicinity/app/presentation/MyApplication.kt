package com.cjproductions.vicinity.app.presentation

import android.app.Application
import com.cjproductions.vicinity.app.di.initializeKoin
import org.koin.android.ext.koin.androidContext

class MyApplication: Application() {
  override fun onCreate() {
    super.onCreate()
    initializeKoin(config = { androidContext(this@MyApplication) })
  }
}