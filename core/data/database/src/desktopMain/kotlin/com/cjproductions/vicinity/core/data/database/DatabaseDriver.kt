package com.cjproductions.vicinity.core.data.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver

actual class DatabaseDriver() {
  actual val driver: SqlDriver = createDriver()
}

private fun createDriver(): SqlDriver {
  val dbPath = "${System.getProperty("java.io.tmpdir")}/vicinity_temp.db"
  return JdbcSqliteDriver("jdbc:sqlite:$dbPath").also { driver ->
    try {
      VicinityDatabase.Schema.create(driver)
    } catch (e: Exception) {
      println("Database schema already exists: ${e.message}")
    }
  }
}