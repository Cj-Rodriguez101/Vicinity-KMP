package com.cjproductions.vicinity.app.presentation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavUri
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.dialog
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import androidx.navigation.navOptions
import androidx.window.core.layout.WindowWidthSizeClass
import com.cjproductions.vicinity.app.presentation.MainViewAction.OnPauseRealTimeConnection
import com.cjproductions.vicinity.app.presentation.MainViewAction.OnResumeRealTimeConnection
import com.cjproductions.vicinity.app.presentation.Route.AuthGraph
import com.cjproductions.vicinity.app.presentation.Route.ChangePasswordRoot
import com.cjproductions.vicinity.app.presentation.Route.CountrySelectRoot
import com.cjproductions.vicinity.app.presentation.Route.DatePickerRoot
import com.cjproductions.vicinity.app.presentation.Route.DiscoverGraph
import com.cjproductions.vicinity.app.presentation.Route.EditProfileRoot
import com.cjproductions.vicinity.app.presentation.Route.EventDatesRoot
import com.cjproductions.vicinity.app.presentation.Route.EventDetailRoot
import com.cjproductions.vicinity.app.presentation.Route.FilterRoot
import com.cjproductions.vicinity.app.presentation.Route.ForgotPasswordRoot
import com.cjproductions.vicinity.app.presentation.Route.GlobalEventsRoot
import com.cjproductions.vicinity.app.presentation.Route.LikesRoot
import com.cjproductions.vicinity.app.presentation.Route.LocationSelectorRoot
import com.cjproductions.vicinity.app.presentation.Route.LogOutRoot
import com.cjproductions.vicinity.app.presentation.Route.LoginRoot
import com.cjproductions.vicinity.app.presentation.Route.OnboardingRoot
import com.cjproductions.vicinity.app.presentation.Route.ProfileGraph
import com.cjproductions.vicinity.app.presentation.Route.ProfileRoot
import com.cjproductions.vicinity.app.presentation.Route.RegisterRoot
import com.cjproductions.vicinity.app.presentation.Route.SearchGraph
import com.cjproductions.vicinity.app.presentation.Route.SearchResultRoot
import com.cjproductions.vicinity.app.presentation.Route.SearchRoot
import com.cjproductions.vicinity.app.presentation.Route.TrendingRoot
import com.cjproductions.vicinity.app.presentation.Route.VerifyUserRoot
import com.cjproductions.vicinity.auth.presentation.changePassword.ChangePasswordScreenRoot
import com.cjproductions.vicinity.auth.presentation.forgotPassword.ForgotPasswordScreenRoot
import com.cjproductions.vicinity.auth.presentation.logOut.LogOutScreenRoot
import com.cjproductions.vicinity.auth.presentation.login.LoginScreenRoot
import com.cjproductions.vicinity.auth.presentation.onboarding.OnboardingScreenRoot
import com.cjproductions.vicinity.auth.presentation.register.RegisterScreenRoot
import com.cjproductions.vicinity.auth.presentation.verifyUser.VerifyUserScreenRoot
import com.cjproductions.vicinity.core.presentation.ui.RealTimeConnectionLifecycleHandler
import com.cjproductions.vicinity.core.presentation.ui.SnackBarEvent
import com.cjproductions.vicinity.core.presentation.ui.components.RadiusBottomSheet
import com.cjproductions.vicinity.core.presentation.ui.components.RadiusIcon
import com.cjproductions.vicinity.core.presentation.ui.observeAsEvents
import com.cjproductions.vicinity.core.presentation.ui.theme.VicinityTheme
import com.cjproductions.vicinity.discover.presentation.countrySelector.CountrySelectorScreenRoot
import com.cjproductions.vicinity.discover.presentation.discover.DiscoverScreenRoot
import com.cjproductions.vicinity.eventDetail.presentation.eventDates.EventDateScreenRoot
import com.cjproductions.vicinity.eventDetail.presentation.eventDetail.EventDetailScreenRoot
import com.cjproductions.vicinity.eventDetail.presentation.eventDetail.EventDetailViewModel.Companion.ENCODED_TITLE_KEY
import com.cjproductions.vicinity.geoEvents.presentation.globalEvents.GlobalEventScreenRoot
import com.cjproductions.vicinity.geoEvents.presentation.locationSelector.LocationSelectorScreenRoot
import com.cjproductions.vicinity.likes.presentation.likes.LikeScreenRoot
import com.cjproductions.vicinity.likes.presentation.trending.TrendingScreenRoot
import com.cjproductions.vicinity.profile.presentation.profile.editProfile.EditProfileScreenRoot
import com.cjproductions.vicinity.profile.presentation.profile.profileDisplay.ProfileScreenRoot
import com.cjproductions.vicinity.search.presentation.datePicker.DATE_PICKER_KEY
import com.cjproductions.vicinity.search.presentation.datePicker.DatePickerRoot
import com.cjproductions.vicinity.search.presentation.filter.FilterScreenRoot
import com.cjproductions.vicinity.search.presentation.filter.FilterViewModel
import com.cjproductions.vicinity.search.presentation.search.SearchScreenRoot
import com.cjproductions.vicinity.search.presentation.searchResults.SearchResultScreenRoot
import com.cjproductions.vicinity.support.share.presentation.BASE_DEEP_LINK_URL
import com.cjproductions.vicinity.support.share.presentation.EVENT_DETAIL_PATH
import com.cjproductions.vicinity.support.tools.toDouble
import compose.icons.FontAwesomeIcons
import compose.icons.LineAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Globe
import compose.icons.fontawesomeicons.solid.Home
import compose.icons.fontawesomeicons.solid.User
import compose.icons.lineawesomeicons.ChartBar
import compose.icons.lineawesomeicons.HeartSolid
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import vicinity.core.presentation.ui.generated.resources.Res
import vicinity.core.presentation.ui.generated.resources.discover
import vicinity.core.presentation.ui.generated.resources.global
import vicinity.core.presentation.ui.generated.resources.likes
import vicinity.core.presentation.ui.generated.resources.profile
import vicinity.core.presentation.ui.generated.resources.trending

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {
  VicinityTheme {
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()
    val currentDestination: MutableState<String?> = rememberSaveable { mutableStateOf(null) }
    val mainViewModel: MainViewModel = koinViewModel()
    val isLoggedIn by mainViewModel.isLoggedIn.collectAsStateWithLifecycle()

    DisposableEffect(Unit) {
      ExternalUriHandler.listener = { uri -> navController.navigate(NavUri(uri)) }
      onDispose { ExternalUriHandler.listener = null }
    }

    RealTimeConnectionLifecycleHandler(
      networkStatusUpdates = mainViewModel.networkStatusUpdates,
      onPauseRealTimeConnection = { mainViewModel.onAction(OnPauseRealTimeConnection) },
      onResumeRealTimeConnection = { mainViewModel.onAction(OnResumeRealTimeConnection) },
    )

    mainViewModel.events.observeAsEvents(keys = arrayOf(snackBarHostState)) { event ->
      scope.launch {
        snackBarHostState.currentSnackbarData?.dismiss()
        val event = (event as? SnackBarEvent.SnackBarUiEvent) ?: return@launch

        val result = snackBarHostState.showSnackbar(
          message = event.message.getString(),
          actionLabel = event.action?.name,
        )

        if (result == SnackbarResult.ActionPerformed) {
          event.action?.action?.invoke()
        }
      }
    }
    Scaffold(
      snackbarHost = {
        SnackbarHost(
          hostState = snackBarHostState,
          modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .clip(MaterialTheme.shapes.medium),
        )
      },
    ) {
      val destinationsWithVisibleBottomNav = listOfNotNull(
        DiscoverGraph::class.getRouteName(),
        AuthGraph::class.getRouteName(),
        CountrySelectRoot::class.getRouteName(),
        LogOutRoot::class.getRouteName(),
        LikesRoot::class.getRouteName(),
        TrendingRoot::class.getRouteName(),
        GlobalEventsRoot::class.getRouteName(),
        ProfileRoot::class.getRouteName().takeIf { isLoggedIn },
      )
      val navigationSuiteItemColors = navigationSuiteItemColors()
      NavigationSuiteScaffold(
        layoutType = if (destinationsWithVisibleBottomNav.contains(currentDestination.value)) {
          when {
            currentWindowAdaptiveInfo().windowSizeClass
              .windowWidthSizeClass == WindowWidthSizeClass.COMPACT
              -> NavigationSuiteType.NavigationBar

            else -> NavigationSuiteType.NavigationRail
          }
        } else NavigationSuiteType.None,
        navigationSuiteItems = {
          suiteItem(
            currentDestination = currentDestination,
            navigationSuiteItemColors = navigationSuiteItemColors,
            route = DiscoverGraph,
            onClick = {
              if (currentDestination.value == DiscoverGraph::class.getRouteName()) return@suiteItem
              navController.navigate(
                route = DiscoverGraph,
                navOptions = navController.getBottomNavOptions(),
              )
            },
            label = { Text(text = stringResource(Res.string.discover)) },
            icon = {
              RadiusIcon(
                icon = FontAwesomeIcons.Solid.Home,
                contentDescription = stringResource(Res.string.discover),
              )
            },
          )

          suiteItem(
            currentDestination = currentDestination,
            navigationSuiteItemColors = navigationSuiteItemColors,
            route = ProfileRoot,
            onClick = {
              if (currentDestination.value in listOf(
                  ProfileRoot::class.getRouteName(),
                  AuthGraph::class.getRouteName(),
                )
              ) return@suiteItem
              val destination = ProfileGraph.takeIf { isLoggedIn } ?: AuthGraph
              navController.navigate(
                route = destination,
                navOptions = navController.getBottomNavOptions(),
              )
            },
            label = { Text(text = stringResource(Res.string.profile)) },
            icon = {
              RadiusIcon(
                icon = FontAwesomeIcons.Solid.User,
                contentDescription = stringResource(Res.string.profile),
              )
            },
          )

          suiteItem(
            currentDestination = currentDestination,
            navigationSuiteItemColors = navigationSuiteItemColors,
            route = LikesRoot,
            onClick = {
              if (currentDestination.value == LikesRoot::class.getRouteName()) return@suiteItem
              navController.navigate(
                route = LikesRoot,
                navOptions = navController.getBottomNavOptions(),
              )
            },
            label = { Text(text = stringResource(Res.string.likes)) },
            icon = {
              RadiusIcon(
                icon = LineAwesomeIcons.HeartSolid,
                contentDescription = stringResource(Res.string.likes),
              )
            },
          )

          if (isLoggedIn) {
            suiteItem(
              currentDestination = currentDestination,
              navigationSuiteItemColors = navigationSuiteItemColors,
              route = TrendingRoot,
              onClick = {
                if (currentDestination.value == TrendingRoot::class.getRouteName()) return@suiteItem
                navController.navigate(
                  route = TrendingRoot,
                  navOptions = navController.getBottomNavOptions(),
                )
              },
              label = { Text(text = stringResource(Res.string.trending)) },
              icon = {
                RadiusIcon(
                  icon = LineAwesomeIcons.ChartBar,
                  contentDescription = stringResource(Res.string.trending),
                )
              },
            )
          }

          if (isLoggedIn) {
            suiteItem(
              currentDestination = currentDestination,
              navigationSuiteItemColors = navigationSuiteItemColors,
              route = GlobalEventsRoot,
              onClick = {
                if (currentDestination.value == GlobalEventsRoot::class.getRouteName()) return@suiteItem
                navController.navigate(
                  route = GlobalEventsRoot,
                  navOptions = navController.getBottomNavOptions(),
                )
              },
              label = { Text(text = stringResource(Res.string.global)) },
              icon = {
                RadiusIcon(
                  icon = FontAwesomeIcons.Solid.Globe,
                  contentDescription = stringResource(Res.string.global),
                )
              },
            )
          }
        },
        navigationSuiteColors = NavigationSuiteDefaults.colors(
          navigationBarContentColor = Color.Transparent,
          navigationBarContainerColor = MaterialTheme.colorScheme.surface,
          navigationDrawerContainerColor = MaterialTheme.colorScheme.surface,
          navigationDrawerContentColor = Color.Transparent,
          navigationRailContentColor = Color.Transparent,
          navigationRailContainerColor = MaterialTheme.colorScheme.surface,
        ),
      ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        LaunchedEffect(navBackStackEntry) {
          currentDestination.value =
            navBackStackEntry?.destination?.route?.extractBaseRoute()
        }
        NavHost(
          navController = navController,
          startDestination = DiscoverGraph,
        ) {
          navigation<AuthGraph>(
            startDestination = OnboardingRoot,
          ) {
            composable<OnboardingRoot> {
              OnboardingScreenRoot(
                onBackClicked = navController::navigateUp,
                onContinueClicked = { navController.goToRoute(LoginRoot) },
              )
            }

            composable<RegisterRoot> {
              RegisterScreenRoot(
                goToVerification = { email ->
                  navController.goToRoute(VerifyUserRoot(email = email))
                },
                goBack = navController::navigateUp,
                goToLogin = navController::navigateUp,
                goToDiscover = navController::redirectToDiscover,
              )
            }

            composable<LoginRoot> {
              LoginScreenRoot(
                goToDiscover = navController::redirectToDiscover,
                goBack = navController::navigateUp,
                goToRegister = { navController.goToRoute(RegisterRoot) },
                goToForgotPassword = { navController.goToRoute(ForgotPasswordRoot) },
                goToVerification = { email ->
                  navController.goToRoute(VerifyUserRoot(email = email))
                },
              )
            }

            composable<ForgotPasswordRoot> {
              ForgotPasswordScreenRoot(
                onVerificationClick = { email ->
                  navController.goToRoute(
                    VerifyUserRoot(
                      email = email,
                      source = ForgotPasswordRoot::class.simpleName,
                    )
                  )
                },
                onBackClick = navController::navigateUp,
              )
            }

            composable<VerifyUserRoot> {
              VerifyUserScreenRoot(
                goBack = navController::navigateUp,
                goToNextScreen = navController::redirectToDiscover,
              )
            }

            composable<ChangePasswordRoot> {
              ChangePasswordScreenRoot(goBack = navController::navigateUp)
            }
          }

          composable<DiscoverGraph> {
            DiscoverScreenRoot(
              onEventDetail = { title -> navController.navigate(EventDetailRoot(title)) },
              onSearchClick = { navController.goToRoute(SearchRoot) },
              onOpenCountrySelect = { navController.goToRoute(CountrySelectRoot) },
              onBackClick = navController::popBackStack,
              onFilterClick = { navController.goToRoute(FilterRoot) },
            )
          }

          animatedComposable<EventDetailRoot>(
            deepLinks = listOf(
              navDeepLink {
                uriPattern =
                  "$BASE_DEEP_LINK_URL$EVENT_DETAIL_PATH{$ENCODED_TITLE_KEY}"
              }
            )
          ) {
            EventDetailScreenRoot(
              goBack = navController::navigateUp,
              goToEventDates = { eventIds, date ->
                navController.goToRoute(
                  EventDatesRoot(
                    eventIds = eventIds,
                    date = date,
                  ),
                )
              },
            )
          }

          dialog<EventDatesRoot>(
            dialogProperties = DialogProperties(usePlatformDefaultWidth = false)
          ) {
            RadiusBottomSheet(
              onDismiss = navController::navigateUp,
            ) { onDismiss -> EventDateScreenRoot(onDismiss = onDismiss) }
          }

          dialog<CountrySelectRoot>(
            dialogProperties = DialogProperties(usePlatformDefaultWidth = false)
          ) {
            RadiusBottomSheet(
              onDismiss = navController::navigateUp,
            ) { onDismiss ->
              CountrySelectorScreenRoot(onDismiss = onDismiss)
            }
          }

          navigation<ProfileGraph>(startDestination = ProfileRoot) {
            composable<ProfileRoot> {
              ProfileScreenRoot(
                onBackClick = navController::navigateUp,
                onLogOutClick = { navController.goToRoute(LogOutRoot) },
                onEditProfileClick = { navController.goToRoute(EditProfileRoot) },
                onLocationClick = { navController.goToRoute(LocationSelectorRoot) },
                onLikesClick = {
                  navController.goToRoute(
                    route = LikesRoot,
                    navOptions = navController.getBottomNavOptions(),
                  )
                }
              )
            }

            composable<EditProfileRoot> {
              EditProfileScreenRoot(
                onBackClick = navController::navigateUp,
                onLocationClick = { navController.goToRoute(LocationSelectorRoot) },
                onPasswordChangeClick = { navController.goToRoute(ChangePasswordRoot) },
              )
            }

            dialog<LogOutRoot>(
              dialogProperties = DialogProperties(usePlatformDefaultWidth = false)
            ) {
              RadiusBottomSheet(onDismiss = navController::navigateUp) { onDismiss ->
                LogOutScreenRoot(
                  onLogOut = navController::redirectToDiscover,
                  onDismiss = onDismiss,
                )
              }
            }
          }

          navigation<SearchGraph>(
            startDestination = SearchRoot,
          ) {
            animatedComposable<SearchRoot> {
              SearchScreenRoot(
                goBack = { navController.popBackStack() },
                onSearchResult = { -> navController.navigate(route = SearchResultRoot) },
              )
            }

            animatedComposable<SearchResultRoot> {
              SearchResultScreenRoot(
                onBackClick = navController::popBackStack,
                onGoToSearch = {
                  navController.goToRoute(
                    route = SearchRoot,
                    navOptions = navOptions {
                      popUpTo(SearchResultRoot) { inclusive = true }
                      launchSingleTop = true
                    }
                  )
                },
                onFilterClick = { navController.goToRoute(FilterRoot) },
                onEventDetail = { title ->
                  navController.navigate(EventDetailRoot(title))
                }
              )
            }

            animatedComposable<FilterRoot> {
              val filterViewModel =
                koinViewModel<FilterViewModel>(viewModelStoreOwner = it)

              val data by it.savedStateHandle.getStateFlow<Array<Double>>(
                key = DATE_PICKER_KEY,
                initialValue = arrayOf(),
              ).collectAsStateWithLifecycle()

              LaunchedEffect(data) {
                filterViewModel.savedStateHandle[DATE_PICKER_KEY] = data
              }

              FilterScreenRoot(
                viewModel = filterViewModel,
                onGoToDateFilter = { navController.goToRoute(DatePickerRoot(it)) },
                onBackClick = navController::navigateUp,
              )
            }

            composable<DatePickerRoot> {
              DatePickerRoot(
                goBack = navController::navigateUp,
                goToFilter = { dates ->
                  navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set<Array<Double>?>(
                      DATE_PICKER_KEY,
                      dates.map { it.toDouble() }.toTypedArray()
                    )
                  navController.navigateUp()
                }
              )
            }
          }

          composable<LikesRoot> {
            LikeScreenRoot(
              onBackClick = navController::popBackStack,
              onSignInClick = { navController.goToRoute(LoginRoot) },
              onItemClick = { title ->
                navController.navigate(EventDetailRoot(title))
              }
            )
          }

          composable<LocationSelectorRoot> {
            LocationSelectorScreenRoot(onBack = navController::navigateUp)
          }

          composable<TrendingRoot> {
            TrendingScreenRoot(onBack = navController::navigateUp)
          }

          composable<GlobalEventsRoot> {
            GlobalEventScreenRoot(
              onSearchBarClick = { navController.goToRoute(LocationSelectorRoot) },
              onCallOutClick = { title -> navController.navigate(EventDetailRoot(title)) },
            )
          }
        }
      }
    }
  }
}

