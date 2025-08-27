package com.cjproductions.vicinity.core.data.database

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.cjproductions.vicinity.support.tools.DB_NAME

actual class DatabaseDriver(context: Context) {
    actual val driver: SqlDriver = AndroidSqliteDriver(
        schema = VicinityDatabase.Schema,
        context = context.applicationContext,
        name = DB_NAME,
    )
}