import com.cjproductions.convention.configureAndroidNameSpace
import java.util.Properties

plugins {
    alias(libs.plugins.vicinity.application.ktor)
    alias(libs.plugins.gmazzoBuildconfig)
}

kotlin {
    configureAndroidNameSpace("com.cjproductions.vicinity.search.data")
    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)
            api(projects.core.data.network)
            api(projects.core.data.database)
            api(libs.sqldelight.coroutines)
            implementation(projects.core.data.preference)
            implementation(projects.search.domain)
            implementation(projects.location.domain)
            implementation(projects.location.data)
            implementation(projects.support.tools)
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
        "LIQ_BASE_URL",
        "\"${localProperties.getProperty("liq.base.url", "")}\""
    )
    buildConfigField(
        "String",
        "LIQ_API_KEY",
        "\"${localProperties.getProperty("liq.api.key", "")}\""
    )
    buildConfigField("String", "TM_BASE_URL", "\"${localProperties.getProperty("base.url", "")}\"")
    buildConfigField("String", "TM_API_KEY", "\"${localProperties.getProperty("tmapi.key", "")}\"")
}