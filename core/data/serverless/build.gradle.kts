import com.cjproductions.convention.configureAndroidNameSpace
import java.util.Properties

plugins {
    alias(libs.plugins.vicinity.application.libraryTarget)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.gmazzoBuildconfig)
}

kotlin {
    configureAndroidNameSpace("com.cjproductions.vicinity.core.data.serverless")

    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)
            api(libs.bundles.supabase)
            api(libs.firebase.storage)
            api(projects.core.domain.serverless)
        }

        androidMain.dependencies {
            implementation(libs.koin.android)
            api(project.dependencies.platform(libs.android.firebase.bom))
            api(libs.firebase.crashlytics)
        }

        iosMain.dependencies {
            api(libs.firebase.crashlytics)
        }

        desktopMain.dependencies {
            implementation(projects.support.tools)
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
        "SUPABASE_URL",
        "\"${localProperties.getProperty("supabase.url", "")}\""
    )
    buildConfigField(
        "String",
        "SUPABASE_API_KEY",
        "\"${localProperties.getProperty("supabase.api.key", "")}\""
    )

    buildConfig {
        buildConfigField(
            "String",
            "CLIENT_ID",
            "\"${localProperties.getProperty("client.id", "")}\""
        )
    }
    useKotlinOutput { internalVisibility = false }
}
