import com.cjproductions.convention.configureAndroidNameSpace

plugins {
    alias(libs.plugins.vicinity.application.libraryTarget)
}

kotlin {
    configureAndroidNameSpace("com.cjproductions.vicinity.support")
}