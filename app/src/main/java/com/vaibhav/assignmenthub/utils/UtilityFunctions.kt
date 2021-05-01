package com.vaibhav.assignmenthub.utils

import com.google.gson.Gson
import com.vaibhav.assignmenthub.data.models.User
import java.util.*

fun getUserStringForSaving(user: User) = Gson().toJson(user)

fun getUserDataFromString(userData: String) = Gson().fromJson(userData, User::class.java)


fun getDateFromMillis(time: Long): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = time
    val months =
        listOf("Jan", "Feb", "Apr", "May", "June", "July", "Aug", "Sept", "Oct", "Nov", "Dec")
    return "${calendar.get(Calendar.DATE)} ${months[calendar.get(Calendar.MONTH)]}"
}