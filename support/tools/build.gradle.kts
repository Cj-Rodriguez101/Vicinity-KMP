import com.cjproductions.convention.configureAndroidNameSpace

plugins {
    id(libs.plugins.vicinity.application.libraryTarget.get().pluginId)
}

kotlin {
    configureAndroidNameSpace("com.cjproductions.vicinity.support.tools")
    jvm("desktop")
    sourceSets {
        val desktopMain by getting
        commonMain.dependencies {
            implementation(libs.koin.core)
            implementation(libs.kotlin.coroutines.core)
            implementation(libs.kermit.logging)
            implementation(libs.kotlin.date.time)
        }
        androidMain.dependencies {
            implementation(libs.koin.android)
        }

        desktopMain.dependencies {
            implementation(libs.kotlinx.coroutines.swing)
        }
    }
}