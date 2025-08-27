import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class IosPlugin: Plugin<Project> {
  override fun apply(target: Project) {
    target.run {
      pluginManager.apply("org.jetbrains.kotlin.multiplatform")
      extensions.configure<KotlinMultiplatformExtension> {
        listOf(
          iosX64(),
          iosArm64(),
          iosSimulatorArm64()
        ).forEach { iosTarget ->
          iosTarget.binaries.framework {
            baseName = "Vicinity"
            isStatic = true
          }
        }
      }
    }
  }
}
