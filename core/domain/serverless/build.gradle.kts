import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.projects
import org.gradle.kotlin.dsl.sourceSets

plugins {
    id(libs.plugins.vicinity.application.domain.get().pluginId)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.domain)
        }
    }
}