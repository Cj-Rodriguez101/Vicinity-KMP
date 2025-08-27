package com.cjproductions.vicinity.core.data.database.di

import app.cash.sqldelight.db.SqlDriver
import com.cjproductions.vicinity.core.data.database.DatabaseDriver
import com.cjproductions.vicinity.core.data.database.VicinityDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

expect val platformDatabaseModule: Module

val coreDatabaseModule = module {
  includes(platformDatabaseModule)
  single<SqlDriver> { get<DatabaseDriver>().driver }
  single<VicinityDatabase> { VicinityDatabase(driver = get()) }
}