import com.cjproductions.convention.configureAndroidNameSpace

plugins {
    alias(libs.plugins.vicinity.application.feature)
}

kotlin {
    configureAndroidNameSpace("com.cjproductions.vicinity.profile.presentation")

    sourceSets {
        commonMain.dependencies {
            implementation(compose.components.resources)
            implementation(libs.line.awesome.icons)
            implementation(projects.profile.domain)
            implementation(projects.location.domain)
            implementation(projects.support.user)
            implementation(projects.likes.domain)
            api(libs.bundles.fileKit)
        }
    }
}