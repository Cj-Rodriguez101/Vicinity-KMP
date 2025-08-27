import app.cash.sqldelight.gradle.SqlDelightExtension
import com.cjproductions.convention.configureExpectActualClasses
import com.cjproductions.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class SqlDelightPlugin: Plugin<Project> {
  override fun apply(target: Project) {
    target.run {
      pluginManager.run {
        apply("vicinity.application.libraryTarget")
        apply("app.cash.sqldelight")
      }

      extensions.configure<KotlinMultiplatformExtension> {
        targets.configureEach { configureExpectActualClasses() }

        sourceSets.run {
          commonMain.dependencies {
            implementation(libs.findLibrary("sqldelight.coroutines").get())
          }

          androidMain.dependencies {
            implementation(libs.findLibrary("sqldelight.android").get())
          }

          iosMain.dependencies {
            implementation(libs.findLibrary("sqldelight.ios").get())
          }

          val desktopMain = getByName("desktopMain")
          desktopMain.dependencies {
            api(libs.findLibrary("kotlinx.coroutines.swing").get())
            implementation(libs.findLibrary("sqldelight.desktop").get())
          }
        }
      }

      extensions.configure<SqlDelightExtension> {
        databases.run {
          create("VicinityDatabase") {
            packageName.set("com.cjproductions.vicinity.core.data.database")
          }
        }
      }
    }
  }
}