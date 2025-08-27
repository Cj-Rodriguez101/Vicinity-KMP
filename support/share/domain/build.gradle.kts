plugins {
    id(libs.plugins.vicinity.application.domain.get().pluginId)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.domain)
        }
    }
}