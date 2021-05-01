package com.vaibhav.assignmenthub.ui.screens.authentication.register

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
class RegisterViewModel @Inject constructor(private val authRepo: AuthRepo) : ViewModel() {

    private val _registerStatus = Channel<Status<User>>()
    val registerStatus = _registerStatus.receiveAsFlow()


    fun onRegisterPressed(
        email: String,
        password: String,
        name: String,
        rollNo: String,
        ofClass: String,
        isTeacher: Boolean
    ) =
        registerUser(email, password, name, rollNo, ofClass, isTeacher)

    private fun registerUser(
        email: String,
        password: String,
        name: String,
        rollNo: String,
        ofClass: String,
        isTeacher: Boolean
    ) =
        viewModelScope.launch {
            if (validateFields(email, password, name, rollNo)) {
                _registerStatus.send(Status.Loading())
                authRepo.registerUser(email = email,
                    password = password,
                    name = name,
                    roll_no = rollNo,
                    isTeacher = isTeacher,
                    ofClass = ofClass,
                    onSuccessListener = {
                        viewModelScope.launch { _registerStatus.send(Status.Success()) }
                    },
                    onFailureListener = {
                        viewModelScope.launch {
                            _registerStatus.send(
                                Status.Error(
                                    message = it.localizedMessage ?: DEFAULT_ERROR
                                )
                            )
                        }
                    }
                )
            } else {
                _registerStatus.send(Status.Error(message = "Please enter details correctly"))
            }
        }

    private fun validateFields(
        email: String,
        password: String,
        name: String,
        rollNo: String
    ): Boolean {
        return !(email.isEmpty() || email == "" || password.isEmpty() || password == "" ||
                name.isEmpty() || name == "" || rollNo.isEmpty() || rollNo == "")
    }
}