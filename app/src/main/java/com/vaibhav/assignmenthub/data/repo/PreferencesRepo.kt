package com.vaibhav.assignmenthub.data.repo

import android.content.SharedPreferences
import com.vaibhav.assignmenthub.data.models.User
import com.vaibhav.assignmenthub.utils.ON_BOARDING_SAVE_KEY
import com.vaibhav.assignmenthub.utils.USER_SAVE_KEY
import com.vaibhav.assignmenthub.utils.getUserDataFromString
import com.vaibhav.assignmenthub.utils.getUserStringForSaving
import javax.inject.Inject

class PreferencesRepo @Inject constructor(private val sharedPref: SharedPreferences) {


    fun isUserLoggedIn() = sharedPref.contains(USER_SAVE_KEY)

    fun isFirstTime() = sharedPref.getBoolean(ON_BOARDING_SAVE_KEY, true)

    fun setOnBoardingComplete() = sharedPref.edit().putBoolean(ON_BOARDING_SAVE_KEY, false).apply()

    fun getUserData(): User? {
        val userData = sharedPref.getString(USER_SAVE_KEY, null)
        return userData?.let {
            getUserDataFromString(userData)
        }
    }

    fun saveUserData(user: User) {
        val userData = getUserStringForSaving(user)
        sharedPref.edit().putString(USER_SAVE_KEY, userData).apply()
    }

    fun removeUserDate() = sharedPref.edit().remove(USER_SAVE_KEY).apply()
}