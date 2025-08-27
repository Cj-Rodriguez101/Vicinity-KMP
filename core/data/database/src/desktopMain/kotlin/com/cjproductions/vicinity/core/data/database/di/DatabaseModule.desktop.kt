package com.cjproductions.vicinity.core.data.database.di

import com.cjproductions.vicinity.core.data.database.DatabaseDriver
import org.koin.dsl.module

actual val platformDatabaseModule = module { single { DatabaseDriver() } }