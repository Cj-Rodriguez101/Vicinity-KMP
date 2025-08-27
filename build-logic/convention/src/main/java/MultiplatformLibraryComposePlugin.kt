import org.gradle.api.Plugin
import org.gradle.api.Project

class MultiplatformLibraryComposePlugin: Plugin<Project> {
  override fun apply(target: Project) {
    target.run {
      pluginManager.run {
        apply("vicinity.application.libraryTarget")
        apply("org.jetbrains.compose")
        apply("org.jetbrains.kotlin.plugin.compose")
      }
    }
  }
}