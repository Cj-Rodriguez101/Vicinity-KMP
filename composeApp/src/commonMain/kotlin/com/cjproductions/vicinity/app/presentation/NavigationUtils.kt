package com.cjproductions.vicinity.app.presentation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteItemColors
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.cjproductions.vicinity.app.presentation.Route.DiscoverGraph
import com.cjproductions.vicinity.support.tools.Platform
import com.cjproductions.vicinity.support.tools.PlatformType
import kotlin.jvm.JvmSuppressWildcards
import kotlin.reflect.KType

internal fun NavController.redirectToDiscover() {
  goToRoute(
    route = DiscoverGraph,
    navOptions = navOptions { popUpTo(DiscoverGraph) { inclusive = true } },
  )
}

internal fun NavController.goToRoute(
  route: Route,
  navOptions: NavOptions? = null,
) {
  try {
    navigate(
      route = route,
      navOptions = navOptions,
    )
  } catch (ex: Exception) {
    println(ex)
  }
}

internal fun NavigationSuiteScope.suiteItem(
  currentDestination: MutableState<String?>,
  route: Route,
  navigationSuiteItemColors: NavigationSuiteItemColors,
  onClick: () -> Unit,
  label: (@Composable () -> Unit),
  icon: (@Composable () -> Unit),
) {
  item(
    selected = currentDestination.isRouteSelected(route),
    onClick = onClick,
    icon = icon,
    colors = navigationSuiteItemColors,
    label = label,
  )
}

internal fun String.extractBaseRoute(): String {
  return this.substringAfterLast(".")
    .substringBefore("/")
    .substringBefore("{")
    .substringBefore("?")
}

internal fun MutableState<String?>.isRouteSelected(route: Route): Boolean =
  this.value?.contains(route::class.getRouteName()) == true

inline fun <reified T: Any> NavGraphBuilder.animatedComposable(
  typeMap: Map<KType, @JvmSuppressWildcards NavType<*>> = emptyMap(),
  deepLinks: List<NavDeepLink> = emptyList(),
  noinline content: @Composable (AnimatedContentScope.(NavBackStackEntry) -> Unit),
) {
  composable<T>(
    typeMap = typeMap,
    deepLinks = deepLinks,
    enterTransition = { enterTransition() },
    popEnterTransition = { popEnterTransition() },
    exitTransition = { exitTransition() },
    popExitTransition = { popExitTransition() },
    content = content,
  )
}

fun enterTransition() = slideInHorizontally(
  initialOffsetX = { OFFSET },
  animationSpec = tween(TWEEN_DURATION)
).takeUnless { Platform.type == PlatformType.IOS }

fun popEnterTransition() = slideInHorizontally(
  initialOffsetX = { -OFFSET },
  animationSpec = tween(TWEEN_DURATION)
).takeUnless { Platform.type == PlatformType.IOS }

fun exitTransition() = slideOutHorizontally(
  targetOffsetX = { -OFFSET },
  animationSpec = tween(TWEEN_DURATION)
).takeUnless { Platform.type == PlatformType.IOS }

fun popExitTransition() = slideOutHorizontally(
  targetOffsetX = { OFFSET },
  animationSpec = tween(TWEEN_DURATION)
).takeUnless { Platform.type == PlatformType.IOS }

private const val OFFSET = 2000
private const val TWEEN_DURATION = 300