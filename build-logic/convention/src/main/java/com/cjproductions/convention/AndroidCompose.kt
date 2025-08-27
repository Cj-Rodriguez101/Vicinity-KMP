package com.cjproductions.convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureAndroidCompose(
  commonExtension: CommonExtension<*, *, *, *, *, *>
) {
  commonExtension.run {
    dependencies {
      "debugApi"(libs.findLibrary("androidx.compose.ui.tooling.preview").get())
    }
  }
}