import com.cjproductions.convention.configureAndroidNameSpace

plugins {
    alias(libs.plugins.vicinity.application.sqldelight)
}

kotlin {
    configureAndroidNameSpace("com.cjproductions.vicinity.core.data.database")
    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)
            implementation(projects.support.tools)
        }

        androidMain.dependencies {
            implementation(libs.koin.android)
        }
    }
}