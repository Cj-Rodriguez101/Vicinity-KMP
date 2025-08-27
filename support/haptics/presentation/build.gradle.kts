import com.cjproductions.convention.configureAndroidNameSpace
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.projects
import org.gradle.kotlin.dsl.sourceSets

plugins {
    id(libs.plugins.vicinity.application.feature.get().pluginId)
}

kotlin {
    configureAndroidNameSpace("com.cjproductions.vicinity.support.haptics.presentation")

    jvm("desktop")
    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)
            implementation(projects.support.haptics.domain)
        }
        androidMain {
            dependencies {
                implementation(libs.koin.android)
            }
        }
    }
}