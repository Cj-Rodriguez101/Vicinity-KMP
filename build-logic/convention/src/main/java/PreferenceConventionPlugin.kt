import com.cjproductions.convention.configureExpectActualClasses
import com.cjproductions.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class PreferenceConventionPlugin: Plugin<Project> {
  override fun apply(target: Project) {
    target.run {
      pluginManager.run { apply("vicinity.application.libraryTarget") }
      extensions.configure<KotlinMultiplatformExtension> {
        targets.configureEach { configureExpectActualClasses() }
        sourceSets.run {
          commonMain.dependencies {
            api(libs.findLibrary("settings").get())
            api(libs.findLibrary("settings.coroutines").get())
            api(libs.findLibrary("settings.observable").get())
            api(libs.findLibrary("settings.serialization").get())
          }

          androidMain.dependencies {
            api(libs.findLibrary("androidx.preference").get())
          }
        }
      }
    }
  }
}