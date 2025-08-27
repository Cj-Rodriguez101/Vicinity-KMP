import com.cjproductions.convention.configureAndroidNameSpace

plugins {
    id(libs.plugins.vicinity.application.libraryTarget.get().pluginId)
}

kotlin {
    configureAndroidNameSpace("com.cjproductions.vicinity.support.search")
    jvm("desktop")
    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)
            implementation(projects.support.tools)
            implementation(projects.core.domain)
        }
        androidMain.dependencies {
            implementation(libs.koin.android)
        }
    }
}