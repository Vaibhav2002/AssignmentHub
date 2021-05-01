package com.vaibhav.assignmenthub.data.repo

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.vaibhav.assignmenthub.data.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepo @Inject constructor(
    private val auth: FirebaseAuth,
    private val fireStore: FirebaseFirestore,
    private val preferencesRepo: PreferencesRepo,

    ) {


    fun getUserData() = preferencesRepo.getUserData()!!

    fun isUserLoggedIn() = auth.currentUser != null


    suspend fun logoutUser() {
        withContext(Dispatchers.IO) {
            auth.signOut()
            preferencesRepo.removeUserDate()
        }
    }

    suspend fun loginUser(
        email: String,
        password: String,
        onSuccessListener: () -> Unit,
        onFailureListener: (Exception) -> Unit
    ) = withContext(Dispatchers.IO) {
        try {
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    getUserDetails(
                        it.user?.uid ?: "", onSuccessListener = { user ->
                            preferencesRepo.saveUserData(user)
                            onSuccessListener()
                        },
                        onFailureListener = {
                            onFailureListener(Exception("Failed to login user"))
                        }
                    )
                }
                .addOnFailureListener {
                    onFailureListener(it)
                }
        } catch (e: Exception) {
            onFailureListener(e)
        }

    }


    suspend fun registerUser(
        email: String,
        password: String,
        roll_no: String,
        name: String,
        isTeacher: Boolean,
        ofClass: String,
        onSuccessListener: () -> Unit,
        onFailureListener: (Exception) -> Unit
    ) = withContext(Dispatchers.IO) {
        val user = User(
            name = name,
            email = email,
            rollNo = roll_no,
            teacher = isTeacher,
            ofClass = ofClass
        )
        try {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    user.id = it?.user?.uid ?: ""
                    preferencesRepo.saveUserData(user)
                    addUserToFireStore(user, onSuccessListener, onFailureListener)
                }
                .addOnFailureListener {
                    onFailureListener(it)
                }
        } catch (e: Exception) {
            onFailureListener(e)
        }
    }


    private fun addUserToFireStore(
        user: User,
        onSuccessListener: () -> Unit,
        onFailureListener: (Exception) -> Unit
    ) {
        fireStore.collection("users").document(user.id)
            .set(user)
            .addOnSuccessListener {
                onSuccessListener()
            }
            .addOnFailureListener(onFailureListener)
    }


    private fun getUserDetails(
        uid: String,
        onSuccessListener: (User) -> Unit,
        onFailureListener: (Exception) -> Unit
    ) {
        fireStore.collection("users").whereEqualTo("id", uid)
            .get()
            .addOnSuccessListener {
                for (d in it.documents) {
                    val user = d.toObject(User::class.java)
                    onSuccessListener(user!!)
                }
            }
            .addOnFailureListener(onFailureListener)
    }

}