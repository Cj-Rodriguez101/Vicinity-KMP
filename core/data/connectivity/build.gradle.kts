@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import com.cjproductions.convention.configureAndroidNameSpace
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.vicinity.application.libraryTarget)
}

kotlin {
    configureAndroidNameSpace("com.cjproductions.vicinity.core.data.connectivity")

    applyDefaultHierarchyTemplate {
        common {
            group("mobile") {
                group("ios")
                group("android")
            }
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)
            implementation(libs.connectivity.core)
            implementation(libs.connectivity.compose)
            implementation(projects.support.tools)
        }

        get("mobileMain").dependencies {
            implementation(libs.connectivity.device)
            implementation(libs.connectivity.compose.device)
        }
        androidMain {
            dependsOn(get("mobileMain"))
            dependencies { implementation(libs.koin.android) }
        }

        desktopMain.dependencies {
            implementation(libs.connectivity.http)
            implementation(libs.connectivity.compose.http)
        }
    }
}