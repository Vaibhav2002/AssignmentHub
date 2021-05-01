package com.vaibhav.assignmenthub.ui.screens.onBoardingScreen

import androidx.lifecycle.ViewModel
import com.vaibhav.assignmenthub.data.repo.PreferencesRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(private val preferencesRepo: PreferencesRepo) :
    ViewModel() {

    fun setOnBoardingComplete() = preferencesRepo.setOnBoardingComplete()

}