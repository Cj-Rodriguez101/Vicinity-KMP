import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.vicinity.application)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.googleServices)
    alias(libs.plugins.androidCrashlytics)
}

dependencies {
    debugApi(compose.uiTooling)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_1_8)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    jvm("desktop")

    sourceSets {
        val desktopMain by getting
        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
            implementation(libs.koin.android)
            implementation(libs.core.splashscreen)
        }
        commonMain.dependencies {
            implementation(compose.material3)
            implementation(projects.discover.presentation)
            implementation(projects.discover.data)

            implementation(projects.auth.domain)
            api(projects.auth.data)
            implementation(projects.auth.presentation)

            implementation(projects.core.presentation.ui)
            implementation(projects.core.data.preference)
            implementation(projects.core.data.database)
            implementation(projects.core.data.network)
            implementation(projects.core.data.serverless)
            implementation(projects.core.data.connectivity)

            implementation(projects.location.data)
            implementation(projects.location.domain)
            implementation(projects.location.presentation)

            implementation(projects.eventDetail.data)
            implementation(projects.eventDetail.domain)
            implementation(projects.eventDetail.presentation)

            implementation(projects.likes.data)
            implementation(projects.likes.domain)
            implementation(projects.likes.presentation)

            implementation(projects.profile.presentation)

            implementation(projects.support.tools)
            implementation(projects.support.permissions)
            implementation(projects.support.search)
            implementation(projects.support.user)
            implementation(projects.support.share.presentation)
            implementation(projects.support.haptics.presentation)
            implementation(projects.support.share.domain)
            implementation(projects.support.haptics.domain)

            implementation(projects.search.data)
            implementation(projects.search.domain)
            implementation(projects.search.presentation)

            implementation(projects.profile.data)
            implementation(projects.profile.domain)
            implementation(projects.profile.presentation)

            implementation(projects.geoEvents.presentation)

            implementation(libs.navigation.compose)
            implementation(compose.material3AdaptiveNavigationSuite)
            implementation(libs.material3.adaptive)
        }

        desktopMain.dependencies {
            implementation(libs.navigation.suite.desktop)
        }
    }
}

compose.desktop {
    application {
        mainClass = "com.cjproductions.vicinity.MainKt"

        jvmArgs("--add-opens", "java.desktop/sun.awt=ALL-UNNAMED")
        jvmArgs(
            "--add-opens",
            "java.desktop/java.awt.peer=ALL-UNNAMED"
        )

        if (System.getProperty("os.name").contains("Mac")) {
            jvmArgs("--add-opens", "java.desktop/sun.lwawt=ALL-UNNAMED")
            jvmArgs("--add-opens", "java.desktop/sun.lwawt.macosx=ALL-UNNAMED")
        }

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.cjproductions.vicinity"
            packageVersion = "1.0.0"

            // Add URL scheme registration
            macOS {
                bundleID = "com.cjproductions.vicinity"
                infoPlist {
                    extraKeysRawXml = """
                        <key>CFBundleURLTypes</key>
                        <array>
                            <dict>
                                <key>CFBundleURLName</key>
                                <string>Vicinity Deep Link</string>
                                <key>CFBundleURLSchemes</key>
                                <array>
                                    <string>vicinity</string>
                                </array>
                            </dict>
                        </array>
                    """.trimIndent()
                }
            }
        }
    }
}