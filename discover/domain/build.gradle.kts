plugins {
    alias(libs.plugins.vicinity.application.domain)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.domain)
            implementation(projects.location.domain)
            implementation(libs.compose.paging.common)
            api(projects.support.search)
        }
    }
}