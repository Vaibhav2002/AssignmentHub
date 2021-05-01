package com.vaibhav.assignmenthub.data.models

data class User(
    val name: String = "",
    val email: String = "",
    val rollNo: String = "",
    val ofClass: String = "",
    val teacher: Boolean = false,
) {
    var id = ""
}
