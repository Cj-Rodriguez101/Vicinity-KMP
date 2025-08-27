@file:OptIn(ExperimentalUuidApi::class)

package com.cjproductions.vicinity.auth.data

import com.cjproductions.vicinity.support.user.Provider
import com.cjproductions.vicinity.support.user.User
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.OtpType
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.auth.user.UserSession
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi

interface AuthDataSource {
  suspend fun signUpWithEmailPassword(
    username: String,
    email: String,
    password: String,
  )

  suspend fun signInWithEmailPassword(
    email: String,
    password: String,
  )

  suspend fun signInWithGoogle()

  suspend fun changeDisplayName(displayName: String)

  suspend fun refreshSession()

  suspend fun logOut()

  suspend fun getUser(): User?

  suspend fun verifyTokenHash(
    tokenHash: String,
    otpType: OtpType.Email = OtpType.Email.SIGNUP,
  )

  suspend fun resetPassword(email: String)

  suspend fun changePassword(password: String)

  suspend fun resendVerificationCode(email: String, type: OtpType.Email)

  suspend fun setAuthSession(
    accessToken: String,
    refreshToken: String,
    providerToken: String?,
    expiresIn: Long,
    expiresAt: Long?,
  )

  val sessionStatus: StateFlow<SessionStatus>
}

class DefaultAuthDataSource(
  private val supabaseClient: SupabaseClient,
): AuthDataSource {
  override suspend fun signUpWithEmailPassword(
    username: String,
    email: String,
    password: String,
  ) {
    supabaseClient.auth.signUpWith(Email) {
      this.email = email
      this.password = password
      this.data = buildJsonObject {
        put(
          key = FULL_NAME,
          value = username,
        )
      }
    }
  }

  override suspend fun signInWithEmailPassword(email: String, password: String) {
    supabaseClient.auth.signInWith(Email) {
      this.email = email
      this.password = password
    }
  }

  override suspend fun signInWithGoogle() {
    supabaseClient.auth.signInWith(Google)
  }

  override suspend fun changeDisplayName(displayName: String) {
    supabaseClient.auth.updateUser {
      this.data = buildJsonObject {
        put(
          key = FULL_NAME,
          value = displayName,
        )
      }
    }
  }

  override suspend fun refreshSession() {
    supabaseClient.auth.refreshCurrentSession()
  }

  override suspend fun logOut() {
    supabaseClient.auth.signOut()
  }

  @OptIn(ExperimentalTime::class)
  override suspend fun getUser(): User? {
    return supabaseClient.auth.retrieveUserForCurrentSession(updateSession = false)
      .let { userInfo ->
        User(
          id = userInfo.id,
          username = userInfo.userMetadata?.get(FULL_NAME).asString()
            .takeIf { it.isNotEmpty() } ?: userInfo.userMetadata?.get(DISPLAY_NAME)
            .asString(),
          email = userInfo.email.orEmpty(),
          provider = when (userInfo.appMetadata?.get(PROVIDER).asString()) {
            GOOGLE_PROVIDER -> Provider.Google
            else -> Provider.Email
          },
          createdAt = userInfo.createdAt?.toLocalDateTime(TimeZone.currentSystemDefault()),
        )
      }
  }

  override suspend fun verifyTokenHash(
    tokenHash: String,
    otpType: OtpType.Email,
  ) {
    supabaseClient.auth.verifyEmailOtp(
      type = otpType,
      tokenHash = tokenHash,
    )
  }

  override suspend fun resetPassword(email: String) {
    supabaseClient.auth.resetPasswordForEmail(email)
  }

  override suspend fun changePassword(password: String) {
    supabaseClient.auth.updateUser { this.password = password }
  }

  override suspend fun resendVerificationCode(email: String, type: OtpType.Email) {
    supabaseClient.auth.resendEmail(
      type = type,
      email = email,
    )
  }

  override suspend fun setAuthSession(
    accessToken: String,
    refreshToken: String,
    providerToken: String?,
    expiresIn: Long,
    expiresAt: Long?,
  ) {
    try {
      val sessionExpiresAt = expiresAt?.let {
        Instant.fromEpochSeconds(it)
      } ?: Clock.System.now().plus(expiresIn.seconds)

      val userSession = UserSession(
        accessToken = accessToken,
        refreshToken = refreshToken,
        providerRefreshToken = null,
        providerToken = providerToken,
        expiresIn = expiresIn,
        tokenType = TOKEN_TYPE,
        user = null,
        type = SESSION_TYPE,
        expiresAt = sessionExpiresAt
      )

      supabaseClient.auth.importSession(
        session = userSession,
        autoRefresh = true
      )

    } catch (e: Exception) {
      println("Failed to set auth session: ${e.message}")
    }
  }

  override val sessionStatus = supabaseClient.auth.sessionStatus

  private fun JsonElement?.asString(): String {
    return this?.let { jsonElement ->
      if (jsonElement is JsonPrimitive && jsonElement.isString) {
        jsonElement.content
      } else null
    }.orEmpty()
  }

  companion object {
    private const val FULL_NAME = "full_name"
    private const val DISPLAY_NAME = "display_name"
    private const val PROVIDER = "provider"
    private const val GOOGLE_PROVIDER = "google"
    private const val SESSION_TYPE = "signup"
    private const val TOKEN_TYPE = "bearer"
  }
}