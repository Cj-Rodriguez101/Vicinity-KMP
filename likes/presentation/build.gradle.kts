import com.cjproductions.convention.configureAndroidNameSpace

plugins {
    alias(libs.plugins.vicinity.application.feature)
}

kotlin {
    configureAndroidNameSpace("com.cjproductions.vicinity.likes.presentation")

    sourceSets {
        commonMain.dependencies {
            implementation(projects.auth.domain)
            implementation(projects.likes.domain)
            implementation(compose.components.resources)
            implementation(projects.support.tools)
            implementation(libs.compose.paging.common)
            implementation(projects.support.share.presentation)
            implementation(projects.support.haptics.presentation)
            implementation(libs.github.koalaplot.charts)
        }
    }
}