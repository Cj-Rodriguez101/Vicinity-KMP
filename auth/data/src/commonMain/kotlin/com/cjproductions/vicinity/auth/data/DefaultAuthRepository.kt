package com.cjproductions.vicinity.auth.data

import com.cjproductions.vicinity.auth.domain.AuthError
import com.cjproductions.vicinity.auth.domain.AuthRepository
import com.cjproductions.vicinity.core.domain.util.Result
import com.cjproductions.vicinity.core.domain.util.executeWithCancellationPassthrough
import com.cjproductions.vicinity.profile.data.user.ProfileUserDataSource
import com.cjproductions.vicinity.profile.data.user.model.UserDataEntity
import com.cjproductions.vicinity.support.tools.AppDispatcher
import com.cjproductions.vicinity.support.user.Provider
import com.cjproductions.vicinity.support.user.User
import com.cjproductions.vicinity.support.user.UserSettingsDataSource
import io.github.jan.supabase.auth.OtpType
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.auth.status.SessionStatus.Authenticated
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.cancellation.CancellationException

class DefaultAuthRepository(
  private val userSettingsDataSource: UserSettingsDataSource,
  private val profileUserDataSource: ProfileUserDataSource,
  private val authDataSource: AuthDataSource,
  private val applicationScope: CoroutineScope,
  private val appDispatcher: AppDispatcher,
): AuthRepository, CoroutineScope {

  override val coroutineContext: CoroutineContext = SupervisorJob() + appDispatcher.io

  init {
    launch {
      authDataSource.sessionStatus.collectLatest { status ->
        when (status) {
          is Authenticated -> {
            applicationScope.launch(appDispatcher.io) {
              executeWithCancellationPassthrough(
                block = {
                  val authUser = authDataSource.getUser()
                  if (authUser == null) return@executeWithCancellationPassthrough
                  val user =
                    profileUserDataSource.getUserDataById(authUser.id)?.toUser()
                      ?: authUser
                  userSettingsDataSource.updateUser(user.copy(authenticated = true))
                  profileUserDataSource.upsertUser(
                    with(user) {
                      UserDataEntity(
                        userId = id,
                        name = username,
                        provider = provider.name,
                        email = email,
                      )
                    }
                  )
                },
                onError = { println("$TAG failed to update auth user $it") }
              )
            }.join()
          }

          is SessionStatus.RefreshFailure -> {
            applicationScope.launch(appDispatcher.io) {
              executeWithCancellationPassthrough(
                block = {
                  userSettingsDataSource.user.value?.let {
                    userSettingsDataSource.updateUser(it.copy(authenticated = false))
                  }
                  delay(2000)
                  authDataSource.refreshSession()
                },
                onError = { println("$TAG Manual refresh also failed: $it") }
              )
            }
          }

          is SessionStatus.Initializing -> {
            applicationScope.launch(appDispatcher.io) {
              executeWithCancellationPassthrough(
                block = {
                  userSettingsDataSource.user.value?.let {
                    userSettingsDataSource.updateUser(it.copy(authenticated = false))
                  }
                },
                onError = { println("$TAG failed to update Initializing user $it") }
              )
            }
          }

          is SessionStatus.NotAuthenticated -> {
            applicationScope.launch(appDispatcher.io) {
              executeWithCancellationPassthrough(
                block = { userSettingsDataSource.updateUser(null) },
                onError = { println("$TAG failed to update NotAuthenticated user $it") }
              )
            }.join()
          }
        }
      }
    }
  }

  override suspend fun signUpWithPassword(
    username: String,
    email: String,
    password: String,
  ): Result<Unit, AuthError> {
    return applicationScope.async(appDispatcher.io) {
      try {
        authDataSource.signUpWithEmailPassword(
          username = username,
          email = email,
          password = password
        )
        Result.Success(Unit)
      } catch (ex: Exception) {
        if (ex is CancellationException) throw ex
        println("$ex auth error")
        Result.Error(AuthError.Unknown(ex.toString()))
      }
    }.await()
  }

  override suspend fun signInWithPassword(
    email: String,
    password: String,
  ): Result<Unit, AuthError> {
    return applicationScope.async(appDispatcher.io) {
      try {
        authDataSource.signInWithEmailPassword(
          email = email,
          password = password
        )
        Result.Success(Unit)
      } catch (ex: Exception) {
        if (ex is CancellationException) throw ex
        Result.Error(AuthError.EmailNotConfirmed)
          .takeIf { ex.toString().contains(EMAIL_NOT_CONFIRMED) == true }
          ?: Result.Error(AuthError.Unknown(ex.toString()))
      }
    }.await()
  }

  override suspend fun signInWithGoogle(): Result<Unit, AuthError> {
    return applicationScope.async(appDispatcher.io) {
      try {
        authDataSource.signInWithGoogle()
        Result.Success(Unit)
      } catch (ex: Exception) {
        if (ex is CancellationException) throw ex
        Result.Error(AuthError.Unknown(ex.toString()))
      }
    }.await()
  }

  override suspend fun verifySignUpTokenHash(tokenHash: String): Result<Unit, AuthError> {
    return applicationScope.async(appDispatcher.io) {
      try {
        authDataSource.verifyTokenHash(tokenHash = tokenHash)
        Result.Success(Unit)
      } catch (ex: Exception) {
        if (ex is CancellationException) throw ex
        println(ex.toString() + "auth error")
        Result.Error(AuthError.Unknown(ex.toString()))
      }
    }.await()
  }

  override suspend fun verifyRecoveryTokenHash(tokenHash: String): Result<Unit, AuthError> {
    return applicationScope.async(appDispatcher.io) {
      try {
        authDataSource.verifyTokenHash(
          tokenHash = tokenHash,
          otpType = OtpType.Email.RECOVERY,
        )
        Result.Success(Unit)
      } catch (ex: Exception) {
        if (ex is CancellationException) throw ex
        println(ex.toString() + "auth error")
        Result.Error(AuthError.Unknown(ex.toString()))
      }
    }.await()
  }

  override suspend fun logOut(): Result<Unit, AuthError> {
    return applicationScope.async(appDispatcher.io) {
      try {
        authDataSource.logOut()
        userSettingsDataSource.updateUser(null)
        Result.Success(Unit)
      } catch (ex: Exception) {
        if (ex is CancellationException) throw ex
        println(ex.toString() + "auth error")
        Result.Error(AuthError.Unknown(ex.toString()))
      }
    }.await()
  }

  override suspend fun resetPassword(email: String): Result<Unit, AuthError> {
    return applicationScope.async(appDispatcher.io) {
      try {
        authDataSource.resetPassword(email)
        Result.Success(Unit)
      } catch (ex: Exception) {
        if (ex is CancellationException) throw ex
        println(ex.toString() + "auth error")
        Result.Error(AuthError.Unknown(ex.toString()))
      }
    }.await()
  }

  override suspend fun changePassword(password: String): Result<Unit, AuthError> {
    return applicationScope.async(appDispatcher.io) {
      try {
        authDataSource.changePassword(password)
        Result.Success(Unit)
      } catch (ex: Exception) {
        if (ex is CancellationException) throw ex
        println(ex.toString() + "auth error")
        Result.Error(AuthError.Unknown(ex.toString()))
      }
    }.await()
  }

  override suspend fun resendSignUpVerificationCode(email: String): Result<Unit, AuthError> {
    return applicationScope.async(appDispatcher.io) {
      try {
        authDataSource.resendVerificationCode(
          email = email,
          type = OtpType.Email.SIGNUP,
        )
        Result.Success(Unit)
      } catch (ex: Exception) {
        if (ex is CancellationException) throw ex
        println(ex.toString() + "auth error")
        Result.Error(AuthError.Unknown(ex.toString()))
      }
    }.await()
  }

  override suspend fun resendForgotPasswordVerificationCode(email: String): Result<Unit, AuthError> {
    return applicationScope.async(appDispatcher.io) {
      try {
        authDataSource.resendVerificationCode(
          email = email,
          type = OtpType.Email.RECOVERY,
        )
        Result.Success(Unit)
      } catch (ex: Exception) {
        if (ex is CancellationException) throw ex
        println(ex.toString() + "auth error")
        Result.Error(AuthError.Unknown(ex.toString()))
      }
    }.await()
  }

  override suspend fun setAuthSession(
    accessToken: String,
    refreshToken: String,
    providerToken: String?,
    expiresIn: String?,
    expiresAt: String?,
  ) {
    applicationScope.async(appDispatcher.io) {
      try {
        authDataSource.setAuthSession(
          accessToken = accessToken,
          refreshToken = refreshToken,
          providerToken = providerToken,
          expiresIn = expiresIn?.toLongOrNull() ?: 3600L,
          expiresAt = expiresAt?.toLongOrNull(),

          )
        Result.Success(Unit)
      } catch (ex: Exception) {
        if (ex is CancellationException) throw ex
        println(ex.toString() + "auth error")
        Result.Error(AuthError.Unknown(ex.toString()))
      }
    }.await()
  }

  override val isLoggedIn: StateFlow<Boolean> =
    userSettingsDataSource.user.map { it != null }.stateIn(
      scope = this,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = false,
    )

  private fun UserDataEntity.toUser() = User(
    id = userId,
    username = name,
    email = email,
    provider = Provider.valueOf(provider),
    authenticated = true,
    createdAt = null
  )

  companion object {
    private const val TAG = "AuthRepository"
    private const val EMAIL_NOT_CONFIRMED = "Email not confirmed"
  }
}