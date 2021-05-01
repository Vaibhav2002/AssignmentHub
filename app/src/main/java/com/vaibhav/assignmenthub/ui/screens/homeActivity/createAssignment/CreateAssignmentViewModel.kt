package com.vaibhav.assignmenthub.ui.screens.homeActivity.createAssignment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaibhav.assignmenthub.data.models.Assignment
import com.vaibhav.assignmenthub.data.models.User
import com.vaibhav.assignmenthub.data.repo.AssignmentRepo
import com.vaibhav.assignmenthub.data.repo.PreferencesRepo
import com.vaibhav.assignmenthub.utils.DEFAULT_ERROR
import com.vaibhav.assignmenthub.utils.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateAssignmentViewModel @Inject constructor(
    private val assignmentRepo: AssignmentRepo,
    private val preferencesRepo: PreferencesRepo
) :
    ViewModel() {

    private val _createAssignmentResponse = MutableLiveData<Status<Assignment>>()
    val createAssignmentResponse: LiveData<Status<Assignment>> = _createAssignmentResponse


    fun getUserData() = preferencesRepo.getUserData()

    fun onSaveClicked(
        subject: String,
        title: String,
        deadline: String,
        forClass: String,
        description: String,
        user: User
    ) {
        if (validate(subject, title, deadline, forClass, description)) {
            val assignment =
                Assignment(subject, title, forClass, user.id, deadline, description = description)
            saveAssignment(assignment)
        } else
            _createAssignmentResponse.postValue(Status.Error("Enter all fields correctly"))
    }


    private fun saveAssignment(assignment: Assignment) = viewModelScope.launch {
        _createAssignmentResponse.postValue(Status.Loading())
        assignmentRepo.addAssignment(assignment,
            onSuccessListener = {
                _createAssignmentResponse.postValue(Status.Success())
            },
            onFailureListener = {
                _createAssignmentResponse.postValue(
                    Status.Error(it.localizedMessage ?: DEFAULT_ERROR)
                )
            }

        )
    }


    private fun validate(
        subject: String,
        title: String,
        deadline: String,
        forClass: String,
        description: String,
    ) =
        !(subject.isEmpty() || title.isEmpty() || deadline.isEmpty() || forClass.isEmpty() || description.isEmpty())

}