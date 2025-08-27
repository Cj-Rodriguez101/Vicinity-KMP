import com.cjproductions.convention.configureAndroidNameSpace

plugins {
    alias(libs.plugins.vicinity.application.feature)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    configureAndroidNameSpace("com.cjproductions.vicinity.search.presentation")

    sourceSets {
        commonMain.dependencies {
            implementation(compose.material3)
            implementation(projects.search.domain)
            implementation(projects.auth.domain)
            implementation(projects.location.domain)
            implementation(projects.discover.domain)
            implementation(libs.compose.paging.common)
            implementation(libs.kotlinx.serialization.json)
            implementation(compose.components.resources)
            implementation(libs.line.awesome.icons)
            implementation(libs.compose.ui.backhandler)
            implementation(libs.compose.calendar)
            implementation(libs.material3.adaptive)
            implementation(projects.likes.domain)
            implementation(projects.support.share.presentation)
            implementation(projects.support.haptics.presentation)
            api(projects.support)
            api(projects.support.search)
        }
    }
}