import com.android.build.api.dsl.ApplicationExtension
import com.cjproductions.convention.ExtensionType
import com.cjproductions.convention.configureBuildTypes
import com.cjproductions.convention.configureKotlinAndroid
import com.cjproductions.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationPlugin: Plugin<Project> {
  override fun apply(target: Project) {
    target.run {
      pluginManager.run {
        apply("com.android.application")
        apply("org.jetbrains.kotlin.multiplatform")
      }
      extensions.configure<ApplicationExtension> {
        namespace = libs.findVersion("projectApplicationId").get().toString()
        defaultConfig {
          applicationId = libs.findVersion("projectApplicationId").get().toString()
          targetSdk = libs.findVersion("projectTargetSdkVersion").get().toString().toInt()

          // Read version at execution time, not configuration time
          versionCode = provider {
            libs.findVersion("projectVersionCode").get().toString().toInt()
          }.get()
          versionName = provider {
            libs.findVersion("projectVersionName").get().toString()
          }.get()

          manifestPlaceholders["MAP_API_KEY"] = "Any Text Here"
        }

        packaging.resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"

        configureKotlinAndroid(this)

        configureBuildTypes(
          commonExtension = this,
          extensionType = ExtensionType.APPLICATION
        )
      }
    }
  }
}