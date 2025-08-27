import com.cjproductions.convention.configureAndroidNameSpace
import java.util.Properties

plugins {
    alias(libs.plugins.vicinity.application.libraryTarget)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.gmazzoBuildconfig)
}

kotlin {
    configureAndroidNameSpace("com.cjproductions.vicinity.profile.data")

    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)
            implementation(libs.sqldelight.coroutines)
            implementation(projects.support.tools)
            implementation(projects.support.user)
            implementation(projects.core.data.serverless)
            implementation(projects.likes.domain)
            implementation(projects.core.domain)
            implementation(projects.profile.domain)
            implementation(libs.filekit.core)
        }

        androidMain.dependencies {
            implementation(libs.koin.android)
        }
    }
}

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localPropertiesFile.inputStream().use { localProperties.load(it) }
}

buildConfig {
    buildConfigField(
        "String",
        "STORAGE_URL",
        "\"${localProperties.getProperty("storage.url", "")}\""
    )
    buildConfigField(
        "String",
        "PROJECT_ID",
        "\"${localProperties.getProperty("project.id", "")}\""
    )
}