import com.android.build.api.dsl.ApplicationExtension
import com.cjproductions.convention.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class AndroidApplicationComposePlugin: Plugin<Project> {

  override fun apply(target: Project) {
    target.run {
      pluginManager.run {
        apply("vicinity.application")
      }

      val extension = extensions.getByType<ApplicationExtension>()
      configureAndroidCompose(extension)
    }
  }
}