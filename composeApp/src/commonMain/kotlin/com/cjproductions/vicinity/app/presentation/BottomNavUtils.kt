package com.cjproductions.vicinity.app.presentation

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteItemColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.navOptions

@Composable
internal fun navigationSuiteItemColors(): NavigationSuiteItemColors {
  val navigationBarItemColors = NavigationBarItemDefaults.colors(
    selectedIconColor = MaterialTheme.colorScheme.primary,
    selectedTextColor = MaterialTheme.colorScheme.primary,
    indicatorColor = Color.Transparent,
    unselectedIconColor = MaterialTheme.colorScheme.onSurface,
    unselectedTextColor = MaterialTheme.colorScheme.onSurface,
    disabledIconColor = Color.Transparent,
    disabledTextColor = Color.Transparent,
  )
  val navigationRailItemColors = NavigationRailItemDefaults.colors(
    selectedIconColor = MaterialTheme.colorScheme.primary,
    selectedTextColor = MaterialTheme.colorScheme.primary,
    unselectedIconColor = MaterialTheme.colorScheme.onSurface,
    unselectedTextColor = MaterialTheme.colorScheme.onSurface,
    indicatorColor = Color.Transparent,
    disabledIconColor = Color.Transparent,
  )
  val navigationDrawerItemColors = NavigationDrawerItemDefaults.colors(
    selectedIconColor = MaterialTheme.colorScheme.primary,
    selectedTextColor = MaterialTheme.colorScheme.primary,
    selectedContainerColor = Color.Transparent,
    unselectedBadgeColor = Color.Transparent,
    unselectedIconColor = MaterialTheme.colorScheme.onSurface,
    unselectedTextColor = MaterialTheme.colorScheme.onSurface,
  )
  val navigationSuiteItemColors = NavigationSuiteItemColors(
    navigationBarItemColors = navigationBarItemColors,
    navigationRailItemColors = navigationRailItemColors,
    navigationDrawerItemColors = navigationDrawerItemColors,
  )
  return navigationSuiteItemColors
}

internal fun NavController.getBottomNavOptions() = navOptions {
  popUpTo(graph.findStartDestination().id) { saveState = true }
  restoreState = true
  launchSingleTop = true
}