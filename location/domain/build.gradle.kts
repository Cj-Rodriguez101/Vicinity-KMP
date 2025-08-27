import com.cjproductions.convention.configureAndroidNameSpace

plugins {
    alias(libs.plugins.vicinity.application.libraryTarget)
}

kotlin {
    configureAndroidNameSpace(
        namespace = "com.cjproductions.vicinity.location.domain",
        compilerArgs = listOf(
            "-P",
            "plugin:org.jetbrains.kotlin.parcelize:additionalAnnotation=the.package.containing.annotation.CommonParcelize"
        )
    )
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.domain)
        }
    }
}