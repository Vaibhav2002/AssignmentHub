package com.vaibhav.assignmenthub.ui.screens.splashScreen

import androidx.lifecycle.ViewModel
import com.vaibhav.assignmenthub.data.repo.AuthRepo
import com.vaibhav.assignmenthub.data.repo.PreferencesRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashVIewModel @Inject constructor(
    private val authRepo: AuthRepo,
    private val preferencesRepo: PreferencesRepo
) : ViewModel() {

    fun isUserLoggedIn() = authRepo.isUserLoggedIn()

    fun isFirstTime() = preferencesRepo.isFirstTime()

}