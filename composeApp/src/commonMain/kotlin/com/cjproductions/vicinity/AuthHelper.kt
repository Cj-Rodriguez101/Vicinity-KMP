package com.cjproductions.vicinity

import com.cjproductions.vicinity.auth.domain.AuthRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

//used on ios to update session state when logged in with Google
object AuthHelper: KoinComponent {
  suspend fun setAuthSession(
    accessToken: String,
    refreshToken: String,
    providerToken: String? = null,
    expiresIn: String? = "3600",
    expiresAt: String? = null,
  ) {
    val authRepository: AuthRepository = get()
    authRepository.setAuthSession(
      accessToken = accessToken,
      refreshToken = refreshToken,
      providerToken = providerToken,
      expiresIn = expiresIn,
      expiresAt = expiresAt,
    )
  }
}