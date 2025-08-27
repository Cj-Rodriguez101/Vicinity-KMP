import com.cjproductions.convention.configureAndroidNameSpace

plugins {
    alias(libs.plugins.vicinity.application.library.compose)
}

compose.resources { publicResClass = true }

kotlin {
    configureAndroidNameSpace("com.cjproductions.vicinity.core.presentation.ui")

    sourceSets {
        val desktopMain by getting
        commonMain.dependencies {
            implementation(projects.likes.domain)
            api(libs.material3.adaptive)
            api(compose.material3)
            api(compose.components.resources)
            api(projects.core.presentation.designsystem)
            api(libs.androidx.lifecycle.viewmodel)
            api(libs.androidx.lifecycle.runtime.compose)
            api(libs.koin.core)
            api(libs.koin.compose.viewmodel.nav)
            api(libs.font.awesome.icons)
            api(libs.line.awesome.icons)
            api(projects.support.tools)
            api(libs.constraint.layout)
            api(libs.flagkit.kmp)
            api(libs.compose.shimmer)
            api(libs.compose.haze)
        }

        androidMain.dependencies {
            implementation(compose.preview)
        }

        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.compose.desktop.tooling)
        }
    }
}