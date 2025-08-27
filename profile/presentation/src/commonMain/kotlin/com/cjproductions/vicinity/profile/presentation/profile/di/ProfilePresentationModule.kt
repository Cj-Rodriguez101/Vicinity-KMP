package com.cjproductions.vicinity.profile.presentation.profile.di

import com.cjproductions.vicinity.profile.domain.DeleteProfileImageUseCase
import com.cjproductions.vicinity.profile.domain.StoreAndUpdateProfileImageUseCase
import com.cjproductions.vicinity.profile.presentation.profile.editProfile.EditProfileViewModel
import com.cjproductions.vicinity.profile.presentation.profile.profileDisplay.ProfileViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val profilePresentationModule = module {
    viewModelOf(::ProfileViewModel)
    viewModelOf(::EditProfileViewModel)
    singleOf(::StoreAndUpdateProfileImageUseCase)
    singleOf(::DeleteProfileImageUseCase)
}