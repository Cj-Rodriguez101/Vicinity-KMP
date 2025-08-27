import com.cjproductions.convention.configureAndroidNameSpace

plugins {
    alias(libs.plugins.vicinity.application.feature)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    configureAndroidNameSpace("com.cjproductions.vicinity.discover.presentation")

    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.ui.backhandler)
            implementation(projects.discover.domain)
            implementation(projects.location.domain)
            implementation(projects.auth.domain)
            implementation(projects.support.permissions)
            implementation(projects.support.user)
            implementation(projects.support.tools)
            implementation(projects.likes.domain)
            implementation(libs.compose.paging.common)
            implementation(compose.components.resources)
            implementation(projects.location.presentation)
            implementation(libs.kotlinx.serialization.json)
            api(projects.support.search)
            implementation(libs.kermit.logging)
            implementation(libs.material3.adaptive)
            implementation(projects.support.share.presentation)
            implementation(projects.support.haptics.presentation)
            implementation(libs.compose.shimmer)
        }
    }
}