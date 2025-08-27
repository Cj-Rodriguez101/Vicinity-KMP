import com.cjproductions.convention.configureKotlin
import com.cjproductions.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class DomainModulePlugin: Plugin<Project> {
  override fun apply(target: Project) {
    target.run {
      pluginManager.run {
        apply("org.jetbrains.kotlin.multiplatform")
        apply("vicinity.application.desktopTarget")
        apply("vicinity.application.iosTarget")
      }
      configureKotlin()

      extensions.configure<KotlinMultiplatformExtension> {
        sourceSets.run {
          commonMain.dependencies {
            api(libs.findLibrary("kotlin.coroutines.core").get())
            api(libs.findLibrary("kotlin.date.time").get())
          }
        }
      }
    }
  }
}