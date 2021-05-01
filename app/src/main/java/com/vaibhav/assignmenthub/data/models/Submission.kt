package com.vaibhav.assignmenthub.data.models

data class Submission(
    val studentId: String = "",
    val assignmentId: String = "",
    val studentName: String = "",
    val timeWhenSubmitted: String = System.currentTimeMillis().toString()
) {
    val submissionId = studentId + timeWhenSubmitted
}