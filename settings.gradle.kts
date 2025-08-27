enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "Vicinity"
pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        maven(url = "https://jitpack.io")
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        maven(url = "https://jitpack.io")
        maven("https://jogamp.org/deployment/maven")
        maven("https://central.sonatype.com/repository/maven-snapshots/")
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

include(":composeApp")
include(":core")
include(":core:domain")
include(":core:data")
include(":core:data:database")
include(":core:data:preference")
include(":core:data:network")
include(":core:data:connectivity")
include(":core:data:serverless")
include(":support")
include(":support:tools")
include(":support:permissions")
include(":support:share")
include(":support:share:domain")
include(":support:share:presentation")
include(":support:haptics:domain")
include(":support:haptics:presentation")
include(":core:presentation")
include(":core:presentation:designsystem")
include(":core:presentation:ui")
include(":discover")
include(":discover:data")
include(":discover:domain")
include(":discover:presentation")
include(":auth")
include(":auth:data")
include(":auth:domain")
include(":auth:presentation")
include(":location")
include(":location:data")
include(":location:domain")
include(":location:presentation")
include(":eventDetail")
include(":eventDetail:data")
include(":eventDetail:domain")
include(":eventDetail:presentation")
include(":profile")
include(":profile:presentation")
include(":search")
include(":search:data")
include(":search:domain")
include(":search:presentation")
include(":support:search")
include(":support:user")
include(":likes")
include(":likes:data")
include(":likes:domain")
include(":likes:presentation")
include(":profile:domain")
include(":profile:data")
include(":core:domain:serverless")
include(":location:presentation:map")
include(":geoEvents")
include(":geoEvents:presentation")
