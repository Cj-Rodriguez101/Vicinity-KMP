plugins {
    alias(libs.plugins.vicinity.application.domain)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.domain)
        }
    }
}