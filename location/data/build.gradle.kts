@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import com.cjproductions.convention.configureAndroidNameSpace
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import java.util.Properties

plugins {
    alias(libs.plugins.vicinity.application.ktor)
    alias(libs.plugins.gmazzoBuildconfig)
}

kotlin {
    configureAndroidNameSpace(
        namespace = "com.cjproductions.vicinity.location.data",
        compilerArgs = listOf(
            "-P",
            "plugin:org.jetbrains.kotlin.parcelize:additionalAnnotation=the.package.containing.annotation.CommonParcelize"
        )
    )
    jvm("desktop")

    applyDefaultHierarchyTemplate {
        common {
            group("mobile") {
                group("ios")
                group("android")
            }
        }
    }

    sourceSets {
        val desktopMain by getting
        commonMain.dependencies {
            implementation(libs.koin.core)
            implementation(projects.location.domain)
            implementation(projects.support.tools)
            implementation(libs.kotlin.coroutines.core)
            implementation(projects.core.data.preference)
            implementation(projects.core.domain)
            implementation(libs.compass.geolocation)
            implementation(libs.compass.geocoder)
            implementation(libs.kermit.logging)
        }

        get("mobileMain").dependencies {
            implementation(libs.compass.permissions.mobile)
            implementation(libs.compass.geolocation.mobile)
            implementation(libs.compass.geocoder.mobile)
        }
        androidMain {
            dependsOn(get("mobileMain"))
            dependencies {
                implementation(libs.koin.android)
                implementation(libs.android.geohash)
            }
        }

        desktopMain.dependencies {
            implementation(libs.kotlinx.coroutines.swing)
            implementation(projects.core.data.network)
            implementation(libs.hsr.geohash)
        }
    }
}

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localPropertiesFile.inputStream().use { localProperties.load(it) }
}

buildConfig {
    buildConfigField(
        "String", "IP_BASE_URL", "\"${localProperties.getProperty("ip.base.url", "")}\""
    )
}