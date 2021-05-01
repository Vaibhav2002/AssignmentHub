package com.vaibhav.assignmenthub.ui.screens.homeActivity.profileScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaibhav.assignmenthub.data.repo.AuthRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val authRepo: AuthRepo) : ViewModel() {

    fun getUserData() = authRepo.getUserData()


    fun onLogoutButtonPressed() = logoutUser()

    private fun logoutUser() = viewModelScope.launch {
        authRepo.logoutUser()
    }

}