import com.cjproductions.convention.configureAndroidNameSpace

plugins {
    alias(libs.plugins.vicinity.application.libraryTarget)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.gmazzoBuildconfig)
}

kotlin {
    configureAndroidNameSpace("com.cjproductions.vicinity.collection.data")

    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)
            implementation(libs.sqldelight.coroutines)
            implementation(projects.support.tools)
            implementation(projects.support.user)
            implementation(projects.core.data.database)
            implementation(projects.core.data.serverless)
            implementation(projects.likes.domain)
            implementation(projects.core.domain)
            api(projects.core.domain.serverless)
            implementation(libs.sqldelight.paging3.extensions)
            implementation(libs.compose.paging.common)
        }

        androidMain.dependencies {
            implementation(libs.koin.android)
        }
    }
}