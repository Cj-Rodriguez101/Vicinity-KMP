import com.cjproductions.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KtorPlugin: Plugin<Project> {
  override fun apply(target: Project) {
    target.run {
      pluginManager.run {
        apply("vicinity.application.libraryTarget")
        apply("org.jetbrains.kotlin.plugin.serialization")
      }

      extensions.configure<KotlinMultiplatformExtension> {
        sourceSets.run {
          val desktopMain = getByName("desktopMain")
          commonMain.dependencies {
            implementation(project(":core:domain"))
            api(libs.findLibrary("ktor.client.core").get())
            api(libs.findLibrary("ktor.serialization.kotlinx.json").get())
            api(libs.findLibrary("ktor.client.logging").get())
            api(libs.findLibrary("ktor.client.content.negotiation").get())
          }

          androidMain.dependencies {
            api(libs.findLibrary("ktor.client.okhttp").get())
          }

          iosMain.dependencies {
            api(libs.findLibrary("ktor.client.darwin").get())
          }

          desktopMain.dependencies {
            api(libs.findLibrary("kotlinx.coroutines.swing").get())
            api(libs.findLibrary("ktor.client.okhttp").get())
          }
        }
      }
    }
  }
}