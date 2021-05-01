package com.vaibhav.assignmenthub.ui.screens.homeActivity.dashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaibhav.assignmenthub.data.models.Assignment
import com.vaibhav.assignmenthub.data.repo.AssignmentRepo
import com.vaibhav.assignmenthub.data.repo.PreferencesRepo
import com.vaibhav.assignmenthub.utils.DEFAULT_ERROR
import com.vaibhav.assignmenthub.utils.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashBoardViewModel @Inject constructor(
    private val assignmentRepo: AssignmentRepo,
    private val preferencesRepo: PreferencesRepo
) :
    ViewModel() {

    private val _assignments = MutableLiveData<Status<List<Assignment>>>()
    val assignments = _assignments

    fun getUser() = preferencesRepo.getUserData()!!

    init {
        fetchAllAssignments()
    }


    fun fetchAllAssignments() = viewModelScope.launch {
        _assignments.postValue(Status.Loading())
        assignmentRepo.getAllAssignments(
            onSuccessListener = {
                _assignments.postValue(Status.Success(it))
            },
            onFailureListener = {
                viewModelScope.launch {
                    _assignments.postValue(Status.Error(it.localizedMessage ?: DEFAULT_ERROR))
                }
            }
        )
    }

}