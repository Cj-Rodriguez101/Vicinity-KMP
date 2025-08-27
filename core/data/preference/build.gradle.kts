import com.cjproductions.convention.configureAndroidNameSpace

plugins {
    alias(libs.plugins.vicinity.application.preference)
}

kotlin {
    configureAndroidNameSpace("com.cjproductions.vicinity.core.data.preference")
    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)
        }

        androidMain.dependencies {
            implementation(libs.koin.android)
        }
    }
}
