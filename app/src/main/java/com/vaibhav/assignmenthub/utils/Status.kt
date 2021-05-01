package com.vaibhav.assignmenthub.utils

sealed class Status<T>(
    val data: T? = null,
    val message: String = ""
) {
    class Loading<T>() : Status<T>()
    class Success<T>(data: T? = null) : Status<T>(data)
    class Error<T>(message: String) : Status<T>(message = message)
}
