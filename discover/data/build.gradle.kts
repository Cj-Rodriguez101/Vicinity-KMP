import com.cjproductions.convention.configureAndroidNameSpace
import java.util.Properties

plugins {
    alias(libs.plugins.vicinity.application.ktor)
    alias(libs.plugins.gmazzoBuildconfig)
}

kotlin {
    configureAndroidNameSpace("com.cjproductions.vicinity.discover.data")
    sourceSets {
        commonMain.dependencies {
            api(projects.core.data.network)
            api(projects.core.data.database)
            api(projects.discover.domain)
            api(projects.location.data)
            implementation(libs.koin.core)
            implementation(projects.core.data.preference)
            implementation(projects.location.domain)
            implementation(libs.sqldelight.paging3.extensions)
            implementation(libs.compose.paging.common)
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
    buildConfigField("String", "API_KEY", "\"${localProperties.getProperty("api.key", "")}\"")
    buildConfigField("String", "BASE_URL", "\"${localProperties.getProperty("base.url", "")}\"")
    useKotlinOutput { internalVisibility = false }
}