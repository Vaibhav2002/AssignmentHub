package com.vaibhav.assignmenthub.ui.screens.homeActivity.assignment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaibhav.assignmenthub.data.models.Submission
import com.vaibhav.assignmenthub.data.repo.PreferencesRepo
import com.vaibhav.assignmenthub.data.repo.SubmissionRepo
import com.vaibhav.assignmenthub.utils.DEFAULT_ERROR
import com.vaibhav.assignmenthub.utils.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AssignmentViewModel @Inject constructor(
    private val submissionRepo: SubmissionRepo,
    private val preferencesRepo: PreferencesRepo
) :
    ViewModel() {

    private val _submissions = MutableLiveData<Status<List<Submission>>>()
    val submissions: LiveData<Status<List<Submission>>> = _submissions

    private val _postSubmission = MutableLiveData<Status<Submission>>()
    val postSubmission: LiveData<Status<Submission>> = _postSubmission

    fun getUser() = preferencesRepo.getUserData()!!

    fun getAllSubmissions(assignmentId: String) = viewModelScope.launch {
        _submissions.postValue(Status.Loading())
        submissionRepo.getStudentsSubmitted(assignmentId,
            onSuccessListener = {
                _submissions.postValue(Status.Success(it))
            },
            onFailureListener = {
                _submissions.postValue(Status.Error(it.localizedMessage ?: DEFAULT_ERROR))
            }
        )
    }

    fun onSubmissionFabPressed(assignmentId: String) = submitAssignment(assignmentId)

    private fun submitAssignment(assignmentId: String) = viewModelScope.launch {
        _postSubmission.postValue(Status.Loading())
        submissionRepo.postSubmission(getUser(), assignmentId,
            onSuccessListener = {
                _postSubmission.postValue(Status.Success(it))
            },
            onFailureListener = {
                _postSubmission.postValue(Status.Error(it.localizedMessage ?: DEFAULT_ERROR))
            }
        )
    }
}