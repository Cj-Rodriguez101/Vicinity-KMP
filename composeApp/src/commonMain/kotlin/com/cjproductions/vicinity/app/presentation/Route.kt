package com.cjproductions.vicinity.app.presentation

import kotlinx.serialization.Serializable
import kotlin.reflect.KClass

sealed interface Route {
  @Serializable
  data object DiscoverGraph: Route

  @Serializable
  data object AuthGraph: Route

  @Serializable
  data object RegisterRoot: Route

  @Serializable
  data object LoginRoot: Route

  @Serializable
  data object ForgotPasswordRoot: Route

  @Serializable
  data object ChangePasswordRoot: Route

  @Serializable
  data class VerifyUserRoot(
    val email: String,
    val source: String? = null,
  ): Route

  @Serializable
  data object OnboardingRoot: Route

  @Serializable
  data object ProfileGraph: Route

  @Serializable
  data object ProfileRoot: Route

  @Serializable
  data object LikesRoot: Route

  @Serializable
  data object SearchGraph: Route

  @Serializable
  data object SearchRoot: Route

  @Serializable
  data object FilterRoot: Route

  @Serializable
  data object EditProfileRoot: Route

  @Serializable
  data class DatePickerRoot(val dates: List<Double>): Route

  @Serializable
  data object SearchResultRoot: Route

  @Serializable
  data class EventDetailRoot(
    val title: String? = null,
    val encodedTitle: String? = null,
  ): Route

  @Serializable
  data class EventDatesRoot(
    val eventIds: List<String>,
    val date: Double,
  ): Route

  @Serializable
  data object CountrySelectRoot: Route

  @Serializable
  data object LogOutRoot: Route

  @Serializable
  data object LocationSelectorRoot: Route

  @Serializable
  data object TrendingRoot: Route

  @Serializable
  data object GlobalEventsRoot: Route
}

internal fun KClass<out Route>.getRouteName(): String {
  return this.simpleName ?: "Unknown"
}