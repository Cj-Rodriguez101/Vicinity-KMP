import com.cjproductions.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class FeatureUiPlugin: Plugin<Project> {
  override fun apply(target: Project) {
    target.run {
      pluginManager.run {
        apply("vicinity.application.library.compose")
      }

      extensions.configure<KotlinMultiplatformExtension> {
        sourceSets.run {
          val desktopMain = getByName("desktopMain")
          commonMain.dependencies {
            implementation(project(":core:presentation:designsystem"))
            api(project(":core:presentation:ui"))

            implementation(libs.findLibrary("koin.core").get())
            implementation(libs.findLibrary("koin.compose.viewmodel.nav").get())
            //api(libs.findLibrary("font.awesome.icons").get())
            //api(libs.findLibrary("line.awesome.icons").get())
          }

          desktopMain.dependencies {
            implementation(libs.findLibrary("kotlinx.coroutines.swing").get())
          }
        }
      }
    }
  }

}