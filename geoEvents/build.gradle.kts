import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.libs

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
}

kotlin {
    androidLibrary {
        namespace = "com.cjproductions.vicinity.geoEvents"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        experimentalProperties["android.experimental.kmp.enableAndroidResources"] = true
    }
}