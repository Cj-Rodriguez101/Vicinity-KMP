import com.cjproductions.convention.configureAndroidNameSpace

plugins {
    alias(libs.plugins.vicinity.application.feature)
}

kotlin {
    configureAndroidNameSpace("com.cjproductions.vicinity.auth.presentation")

    sourceSets {
        commonMain.dependencies {
            implementation(projects.auth.domain)
            implementation(compose.components.resources)
            implementation(libs.line.awesome.icons)
        }
    }
}