import com.cjproductions.convention.configureAndroidNameSpace

plugins {
    id(libs.plugins.vicinity.application.libraryTarget.get().pluginId)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    configureAndroidNameSpace("com.cjproductions.vicinity.support.permissions")
    jvm("desktop")
    sourceSets {
        val desktopMain by getting
        commonMain.dependencies {
            implementation(libs.koin.core)
            implementation(libs.kotlin.coroutines.core)
            implementation(projects.core.presentation.ui)
        }
        androidMain.dependencies {
            implementation(libs.koin.android)
            implementation(libs.moko.permissions)
            implementation(libs.moko.permissions.location)
            implementation(libs.moko.permissions.compose)
        }
        iosMain.dependencies {
            implementation(libs.moko.permissions)
            implementation(libs.moko.permissions.location)
            implementation(libs.moko.permissions.compose)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
        }
    }
}