package com.vaibhav.assignmenthub.data.models

import java.io.Serializable

data class Assignment(
    val subject: String = "",
    val name: String = "",
    val forClass: String = "",
    val teacherId: String = "",
    val deadline: String = "",
    val description: String = "",
    val timeStamp: String = System.currentTimeMillis().toString()
) : Serializable {

    val id = teacherId + name + timeStamp

}
