package com.cjproductions.vicinity.core.data.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.cjproductions.vicinity.support.tools.DB_NAME

actual class DatabaseDriver() {
    actual val driver: SqlDriver = NativeSqliteDriver(
        VicinityDatabase.Schema,
        DB_NAME,
    )

}