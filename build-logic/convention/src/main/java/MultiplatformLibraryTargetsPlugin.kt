import com.cjproductions.convention.configureExpectActualClasses
import com.cjproductions.convention.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class MultiplatformLibraryTargetsPlugin: Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      pluginManager.run {
        apply("org.jetbrains.kotlin.multiplatform")
        apply("com.android.kotlin.multiplatform.library")
        apply("vicinity.application.desktopTarget")
        apply("vicinity.application.iosTarget")
      }

      extensions.configure<KotlinMultiplatformExtension> {
        configureKotlinAndroid(this)
        targets.configureEach { configureExpectActualClasses() }
      }
    }
  }
}