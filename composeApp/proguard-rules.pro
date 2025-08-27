# Your existing rules...
# Keep NavigationSuiteScaffold and related classes
-keep class androidx.compose.material3.adaptive.navigationsuite.** { *; }
-keep class androidx.compose.material3.adaptive.** { *; }
-keep class androidx.compose.material3.windowsizeclass.** { *; }

# Keep all presentation layer classes
-keep class com.cjproductions.vicinity.app.presentation.** { *; }
-keepclassmembers class com.cjproductions.vicinity.app.presentation.** { *; }

# ADD THESE NEW RULES FOR MISSING CLASSES:

# Window size class dependencies
-keep class androidx.window.** { *; }
-dontwarn androidx.window.**

# Adaptive layout dependencies
-keep class androidx.compose.material3.adaptive.layout.** { *; }
-keep class androidx.compose.material3.adaptive.navigation.** { *; }
-dontwarn androidx.compose.material3.adaptive.**

# WindowSizeClass related
-keep class androidx.compose.material3.windowsizeclass.** { *; }
-dontwarn androidx.compose.material3.windowsizeclass.**

# Compose foundation dependencies
-keep class androidx.compose.foundation.** { *; }
-dontwarn androidx.compose.foundation.**

# Activity Compose
-keep class androidx.activity.compose.** { *; }
-dontwarn androidx.activity.compose.**

# Navigation Compose
-keep class androidx.navigation.compose.** { *; }
-dontwarn androidx.navigation.compose.**

# Lifecycle ViewModel Compose
-keep class androidx.lifecycle.viewmodel.compose.** { *; }
-dontwarn androidx.lifecycle.viewmodel.compose.**

# Compose UI dependencies
-keep class androidx.compose.ui.** { *; }
-dontwarn androidx.compose.ui.**

# Compose Runtime
-keep class androidx.compose.runtime.** { *; }
-dontwarn androidx.compose.runtime.**

# Material3 core
-keep class androidx.compose.material3.** { *; }
-dontwarn androidx.compose.material3.**

# Kotlin coroutines
-keep class kotlinx.coroutines.** { *; }
-dontwarn kotlinx.coroutines.**

# Kotlin collections
-keep class kotlin.collections.** { *; }
-dontwarn kotlin.collections.**
