import com.cjproductions.convention.configureAndroidNameSpace

plugins {
    id(libs.plugins.vicinity.application.libraryTarget.get().pluginId)
}

kotlin {
    configureAndroidNameSpace("com.cjproductions.vicinity.favorite.domain")
    jvm("desktop")
    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)
            implementation(projects.auth.domain)
            implementation(projects.core.domain)
            implementation(projects.support.user)
            implementation(projects.support.tools)
            implementation(libs.compose.paging.common)
        }
        androidMain.dependencies {
            implementation(libs.koin.android)
        }
    }
}