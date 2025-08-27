package com.cjproductions.vicinity.core.presentation.ui.di

//import com.cjproductions.vicinity.core.domain.serverless.PauseRealTimeConnectionUseCase
//import com.cjproductions.vicinity.core.domain.serverless.ResumeRealTimeConnectionUseCase
import com.cjproductions.vicinity.core.presentation.ui.SnackBarController
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val corePresentationModule = module {
  singleOf(::SnackBarController)
//    singleOf(::PauseRealTimeConnectionUseCase)
//    singleOf(::ResumeRealTimeConnectionUseCase)
}