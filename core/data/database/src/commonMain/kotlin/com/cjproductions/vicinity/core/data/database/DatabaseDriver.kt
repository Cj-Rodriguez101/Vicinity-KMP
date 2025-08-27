package com.cjproductions.vicinity.core.data.database

import app.cash.sqldelight.db.SqlDriver

expect class DatabaseDriver {
  val driver: SqlDriver
}