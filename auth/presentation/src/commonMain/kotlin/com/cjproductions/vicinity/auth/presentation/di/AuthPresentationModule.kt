package com.cjproductions.vicinity.auth.presentation.di

import com.cjproductions.vicinity.auth.domain.LogOutUseCase
import com.cjproductions.vicinity.auth.domain.LoginUserWithEmailUseCase
import com.cjproductions.vicinity.auth.domain.LoginUserWithGoogleUseCase
import com.cjproductions.vicinity.auth.domain.ObserveLoggedInStateUseCase
import com.cjproductions.vicinity.auth.domain.RegisterUserWithEmailUseCase
import com.cjproductions.vicinity.auth.domain.validator.IsEmailValidUseCase
import com.cjproductions.vicinity.auth.domain.validator.IsPasswordValidUseCase
import com.cjproductions.vicinity.auth.domain.validator.IsUsernameValidUseCase
import com.cjproductions.vicinity.auth.domain.validator.ResendEmailVerificationUseCase
import com.cjproductions.vicinity.auth.presentation.changePassword.ChangePasswordViewModel
import com.cjproductions.vicinity.auth.presentation.forgotPassword.ForgotPasswordViewModel
import com.cjproductions.vicinity.auth.presentation.logOut.LogOutViewModel
import com.cjproductions.vicinity.auth.presentation.login.LoginViewModel
import com.cjproductions.vicinity.auth.presentation.register.RegisterViewModel
import com.cjproductions.vicinity.auth.presentation.verifyUser.DefaultTimerRepository
import com.cjproductions.vicinity.auth.presentation.verifyUser.TimerRepository
import com.cjproductions.vicinity.auth.presentation.verifyUser.VerifyUserViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authPresentationModule = module {
  viewModelOf(::RegisterViewModel)
  viewModelOf(::LoginViewModel)
  viewModelOf(::LogOutViewModel)
  viewModelOf(::ForgotPasswordViewModel)
  viewModelOf(::VerifyUserViewModel)
  viewModelOf(::ChangePasswordViewModel)
  singleOf(::IsEmailValidUseCase)
  singleOf(::ResendEmailVerificationUseCase)
  singleOf(::IsUsernameValidUseCase)
  singleOf(::IsPasswordValidUseCase)
  singleOf(::RegisterUserWithEmailUseCase)
  singleOf(::LoginUserWithEmailUseCase)
  singleOf(::LoginUserWithGoogleUseCase)
  singleOf(::ObserveLoggedInStateUseCase)
  singleOf(::LogOutUseCase)
  factoryOf(::DefaultTimerRepository).bind<TimerRepository>()
}