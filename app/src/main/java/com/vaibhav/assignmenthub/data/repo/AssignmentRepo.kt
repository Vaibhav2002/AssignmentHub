package com.vaibhav.assignmenthub.data.repo

import com.google.firebase.firestore.FirebaseFirestore
import com.vaibhav.assignmenthub.data.models.Assignment
import com.vaibhav.assignmenthub.data.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AssignmentRepo @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val preferencesRepo: PreferencesRepo
) {


    suspend fun getAllAssignments(
        onSuccessListener: (List<Assignment>) -> Unit,
        onFailureListener: (Exception) -> Unit
    ) = withContext(Dispatchers.IO)
    {
        val userData = preferencesRepo.getUserData()
        userData?.let {
            if (it.teacher)
                return@withContext getAssignmentsOfTeacher(it, onSuccessListener, onFailureListener)
            else
                return@withContext getAssignmentOfStudent(it, onSuccessListener, onFailureListener)

        }
    }

    private fun getAssignmentOfStudent(
        user: User,
        onSuccessListener: (List<Assignment>) -> Unit,
        onFailureListener: (Exception) -> Unit
    ) {

        try {
            fireStore.collection("assignments").whereEqualTo("forClass", user.ofClass)
                .get()
                .addOnSuccessListener {
                    val assignments = mutableListOf<Assignment>()
                    for (doc in it.documents) {
                        val assignment = doc.toObject(Assignment::class.java)
                        assignment?.let {
                            assignments.add(assignment)
                        }
                    }
                    onSuccessListener(assignments)
                }
                .addOnFailureListener(onFailureListener)

        } catch (e: Exception) {
            onFailureListener(e)
        }
    }

    private fun getAssignmentsOfTeacher(
        user: User,
        onSuccessListener: (List<Assignment>) -> Unit,
        onFailureListener: (Exception) -> Unit
    ) {
        try {
            fireStore.collection("assignments").whereEqualTo("teacherId", user.id)
                .get()
                .addOnSuccessListener {
                    val assignments = mutableListOf<Assignment>()
                    for (doc in it.documents) {
                        val assignment = doc.toObject(Assignment::class.java)
                        assignment?.let {
                            assignments.add(assignment)
                        }
                    }
                    onSuccessListener(assignments)
                }
                .addOnFailureListener(onFailureListener)

        } catch (e: Exception) {
            onFailureListener(e)
        }
    }


    suspend fun addAssignment(
        assignment: Assignment,
        onSuccessListener: () -> Unit,
        onFailureListener: (Exception) -> Unit
    ) = withContext(Dispatchers.IO) {
        try {
            fireStore.collection("assignments").document(assignment.id)
                .set(assignment)
                .addOnSuccessListener {
                    onSuccessListener()
                }
                .addOnFailureListener(onFailureListener)
        } catch (e: Exception) {
            onFailureListener(e)
        }
    }

}