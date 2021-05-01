package com.vaibhav.assignmenthub.ui.screens.authentication.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaibhav.assignmenthub.data.models.User
import com.vaibhav.assignmenthub.data.repo.AuthRepo
import com.vaibhav.assignmenthub.utils.DEFAULT_ERROR
import com.vaibhav.assignmenthub.utils.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val authRepo: AuthRepo) : ViewModel() {

    private val _loginStatus = Channel<Status<User>>()
    val loginStatus = _loginStatus.receiveAsFlow()


    fun onLoginPressed(email: String, password: String) = loginUser(email, password)

    private fun loginUser(email: String, password: String) = viewModelScope.launch {
        if (validateFields(email, password)) {
            _loginStatus.send(Status.Loading())
            authRepo.loginUser(email, password,
                onSuccessListener = {
                    viewModelScope.launch { _loginStatus.send(Status.Success()) }
                },
                onFailureListener = {
                    viewModelScope.launch {
                        _loginStatus.send(
                            Status.Error(
                                message = it.localizedMessage ?: DEFAULT_ERROR
                            )
                        )
                    }
                }
            )
        } else {
            _loginStatus.send(Status.Error(message = "Please enter details correctly"))
        }
    }

    private fun validateFields(email: String, password: String): Boolean {
        return !(email.isEmpty() || email == "" || password.isEmpty() || password == "")
    }


}