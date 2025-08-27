
import com.cjproductions.convention.configureAndroidNameSpace

plugins {
    alias(libs.plugins.vicinity.application.feature)
}

kotlin {
    configureAndroidNameSpace("com.cjproductions.vicinity.geoEvents.presentation")

    sourceSets {
        commonMain.dependencies {
            implementation(projects.search.domain)
            implementation(projects.location.presentation)
            implementation(compose.components.resources)
        }
    }
}