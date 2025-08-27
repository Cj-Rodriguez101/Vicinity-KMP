import com.cjproductions.convention.configureAndroidNameSpace

plugins {
    id(libs.plugins.vicinity.application.libraryTarget.get().pluginId)
    id(libs.plugins.kotlinSerialization.get().pluginId)
}

kotlin {
    configureAndroidNameSpace("com.cjproductions.vicinity.support.user")
    jvm("desktop")
    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)
            implementation(projects.support.tools)
            implementation(projects.core.domain)
            implementation(projects.core.data.preference)
            implementation(libs.kotlinx.serialization.json)
        }
        androidMain.dependencies {
            implementation(libs.koin.android)
        }
    }
}