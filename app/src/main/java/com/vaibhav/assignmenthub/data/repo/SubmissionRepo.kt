package com.vaibhav.assignmenthub.data.repo

import com.google.firebase.firestore.FirebaseFirestore
import com.vaibhav.assignmenthub.data.models.Submission
import com.vaibhav.assignmenthub.data.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SubmissionRepo @Inject constructor(
    private val fireStore: FirebaseFirestore,
) {


    suspend fun getStudentsSubmitted(
        assignmentId: String,
        onSuccessListener: (List<Submission>) -> Unit,
        onFailureListener: (Exception) -> Unit
    ) = withContext(Dispatchers.IO) {
        try {
            fireStore.collection("submissions").whereEqualTo("assignmentId", assignmentId)
                .get()
                .addOnSuccessListener { query ->
                    val list = mutableListOf<Submission>()
                    for (doc in query.documents) {
                        val submission = doc.toObject(Submission::class.java)
                        submission?.let {
                            list.add(it)
                        }
                    }
                    onSuccessListener(list)
                }
                .addOnFailureListener(onFailureListener)
        } catch (e: Exception) {
            onFailureListener(e)
        }
    }


    suspend fun postSubmission(
        user: User,
        assignmentId: String,
        onSuccessListener: (Submission) -> Unit,
        onFailureListener: (Exception) -> Unit
    ) = withContext(Dispatchers.IO) {
        val submission = Submission(
            user.id, assignmentId, user.name
        )
        try {
            fireStore.collection("submissions").document(submission.submissionId)
                .set(submission)
                .addOnSuccessListener {
                    onSuccessListener(submission)
                }
                .addOnFailureListener(onFailureListener)
        } catch (e: Exception) {
            onFailureListener(e)
        }
    }
}