import com.cjproductions.convention.configureAndroidNameSpace

plugins {
    alias(libs.plugins.vicinity.application.ktor)
}

kotlin {
    configureAndroidNameSpace("com.cjproductions.vicinity.eventDetail.data")
    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)
            implementation(projects.discover.data)
            implementation(projects.eventDetail.domain)
            implementation(projects.location.domain)
            implementation(projects.support.tools)
        }
    }
}