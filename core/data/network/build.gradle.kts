import com.cjproductions.convention.configureAndroidNameSpace

plugins {
    alias(libs.plugins.vicinity.application.ktor)
}

kotlin {
    configureAndroidNameSpace("com.cjproductions.vicinity.core.data.network")
    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)
        }
        androidMain.dependencies {
            implementation(libs.koin.android)
        }
    }
}