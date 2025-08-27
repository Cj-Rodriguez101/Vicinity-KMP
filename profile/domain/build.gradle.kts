plugins {
    alias(libs.plugins.vicinity.application.domain)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.domain)
            implementation(projects.auth.domain)
            implementation(projects.support.user)
            implementation(libs.filekit.core)
        }
    }
}