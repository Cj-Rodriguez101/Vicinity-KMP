plugins {
    `kotlin-dsl`
}

group = "com.cjproductions.vicinity.buildlogic"

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.sqldelight.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "vicinity.application"
            implementationClass = "AndroidApplicationPlugin"
        }
        register("androidApplicationCompose") {
            id = "vicinity.application.compose"
            implementationClass = "AndroidApplicationComposePlugin"
        }
        register("androidLibraryCompose") {
            id = "vicinity.application.library.compose"
            implementationClass = "MultiplatformLibraryComposePlugin"
        }
        register("featureCompose") {
            id = "vicinity.application.feature"
            implementationClass = "FeatureUiPlugin"
        }
        register("libraryTarget") {
            id = "vicinity.application.libraryTarget"
            implementationClass = "MultiplatformLibraryTargetsPlugin"
        }
        register("iosTarget") {
            id = "vicinity.application.iosTarget"
            implementationClass = "IosPlugin"
        }
        register("desktopTarget") {
            id = "vicinity.application.desktopTarget"
            implementationClass = "DesktopPlugin"
        }
        register("sqldelightMultiplatform") {
            id = "vicinity.application.sqldelight"
            implementationClass = "SqlDelightPlugin"
        }
        register("preferenceMultiplatform") {
            id = "vicinity.application.preference"
            implementationClass = "PreferenceConventionPlugin"
        }
        register("domainMultiplatform") {
            id = "vicinity.application.domain"
            implementationClass = "DomainModulePlugin"
        }
        register("kmpKtor") {
            id = "vicinity.application.ktor"
            implementationClass = "KtorPlugin"
        }
    }
}
