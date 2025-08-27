import com.cjproductions.convention.configureAndroidNameSpace

plugins {
    alias(libs.plugins.vicinity.application.libraryTarget)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.gmazzoBuildconfig)
}

kotlin {
    configureAndroidNameSpace("com.cjproductions.vicinity.auth.data")

    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)
            implementation(projects.profile.data)
            implementation(projects.support.tools)
            implementation(projects.support.user)
            api(projects.core.data.preference)
            api(projects.core.data.serverless)
            api(projects.core.domain)
            api(projects.auth.domain)
        }

        androidMain.dependencies {
            implementation(libs.koin.android)
            implementation(libs.androidx.credentials)
            implementation(libs.androidx.credentials)
            implementation(libs.androidx.credentials.play.services.auth)
            implementation(libs.google.identity.googleid)
        }
    }
}