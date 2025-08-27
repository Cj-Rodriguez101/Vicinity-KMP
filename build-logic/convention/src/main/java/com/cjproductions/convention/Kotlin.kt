package com.cjproductions.convention

import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.androidLibrary
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun Project.configureKotlinAndroid(
  commonExtension: CommonExtension<*, *, *, *, *, *>
) {
  commonExtension.apply {
    compileSdk = libs.findVersion("projectCompileSdkVersion").get().toString().toInt()

    defaultConfig.minSdk = libs.findVersion("projectMinSdkVersion").get().toString().toInt()

    compileOptions {
      sourceCompatibility = JavaVersion.VERSION_17
      targetCompatibility = JavaVersion.VERSION_17
    }
  }

  configureKotlin()
}

internal fun Project.configureKotlinAndroid(
  kotlinMultiplatformExtension: KotlinMultiplatformExtension
) {
  kotlinMultiplatformExtension.apply {
    @Suppress("UnstableApiUsage")
    androidLibrary {
      compileSdk = libs.findVersion("projectCompileSdkVersion").get().toString().toInt()
      minSdk = libs.findVersion("projectMinSdkVersion").get().toString().toInt()
      experimentalProperties["android.experimental.kmp.enableAndroidResources"] = true
    }
  }

  configureKotlin()
}

internal fun Project.configureKotlin() {
  tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
      jvmTarget.set(JvmTarget.fromTarget(JavaVersion.VERSION_17.toString()))
    }
  }

}

fun KotlinMultiplatformExtension.configureAndroidNameSpace(
  namespace: String,
  compilerArgs: List<String> = listOf()
) {
  @Suppress("UnstableApiUsage")
  androidLibrary {
    this.namespace = namespace
    compileSdk =
      this.project.libs.findVersion("projectCompileSdkVersion").get().toString().toInt()
    minSdk = this.project.libs.findVersion("projectMinSdkVersion").get().toString().toInt()
    experimentalProperties["android.experimental.kmp.enableAndroidResources"] = true


    compilations.configureEach {
      compileTaskProvider.configure {
        compilerOptions {
          compilerArgs.forEach {
            freeCompilerArgs.add(it)
          }
        }
      }
    }
  }
}

fun KotlinTarget.configureExpectActualClasses() {
  compilations.configureEach {
    compileTaskProvider.configure {
      compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
      }
    }
  }
}