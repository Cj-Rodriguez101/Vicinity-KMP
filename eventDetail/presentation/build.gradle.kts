import com.cjproductions.convention.configureAndroidNameSpace

plugins {
    alias(libs.plugins.vicinity.application.feature)
}

kotlin {
    configureAndroidNameSpace("com.cjproductions.vicinity.eventDetail.presentation")

    sourceSets {
        commonMain.dependencies {
            implementation(projects.eventDetail.domain)
            implementation(projects.support.tools)
            implementation(compose.components.resources)
            implementation(projects.location.presentation)
            implementation(projects.auth.domain)
            implementation(libs.material3.adaptive)
            implementation(projects.location.domain)
            implementation(projects.likes.domain)
            implementation(libs.compose.calendar)
            implementation(projects.support.share.presentation)
            implementation(projects.support.haptics.presentation)
        }
    }
}