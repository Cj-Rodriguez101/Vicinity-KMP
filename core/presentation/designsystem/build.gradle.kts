import com.cjproductions.convention.configureAndroidNameSpace

plugins {
    alias(libs.plugins.vicinity.application.library.compose)
}

kotlin {
    configureAndroidNameSpace("com.cjproductions.vicinity.core.presentation.designsystem")

    sourceSets {
        val desktopMain by getting
        commonMain.dependencies {
            api(compose.runtime)
            api(compose.foundation)
            api(compose.material)
            api(compose.ui)
            api(compose.components.resources)
            api(compose.components.uiToolingPreview)
            api(projects.core.domain)
            api(libs.coil.compose)
            api(libs.coil.network)
        }

        androidMain.dependencies {
            implementation(compose.preview)
        }

        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
        }
    }
}