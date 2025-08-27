import com.cjproductions.convention.configureAndroidNameSpace
import java.util.Properties

plugins {
    alias(libs.plugins.vicinity.application.feature)
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    alias(libs.plugins.gmazzoBuildconfig)
}

kotlin {
    configureAndroidNameSpace("com.cjproductions.vicinity.location.presentation")
    jvm("desktop")

    sourceSets {
        commonMain.dependencies {
            api(projects.location.domain)
            api(libs.plrapps.mapcompose)
            implementation(compose.components.resources)
        }
        androidMain {
            dependencies {
                api(libs.map.android)
                implementation(libs.androidx.activity.compose)
            }
        }
    }
}

secrets {
    propertiesFileName = "secrets.properties"
    defaultPropertiesFileName = "local.defaults.properties"
}

val localProperties = Properties()
val localPropertiesFile = rootProject.file("secrets.properties")
if (localPropertiesFile.exists()) {
    localPropertiesFile.inputStream().use { localProperties.load(it) }
}

buildConfig {
    buildConfigField(
        "String", "MAP_API_KEY", "\"${localProperties.getProperty("MAP_API_KEY", "")}\""
    )
}