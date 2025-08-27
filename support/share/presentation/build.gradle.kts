import com.cjproductions.convention.configureAndroidNameSpace

plugins {
    id(libs.plugins.vicinity.application.feature.get().pluginId)
}

kotlin {
    configureAndroidNameSpace("com.cjproductions.vicinity.support.share.presentation")
    jvm("desktop")
    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)
            implementation(projects.support.share.domain)
            implementation(compose.components.resources)
        }
        androidMain.dependencies {
            implementation(libs.koin.android)
        }
    }
}