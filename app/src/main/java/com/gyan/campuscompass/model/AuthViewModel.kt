package com.gyan.campuscompass.model

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    var currentUser = mutableStateOf<FirebaseUser?>(auth.currentUser)
        private set

    fun signInAnonymously() {
        auth.signInAnonymously().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                currentUser.value = auth.currentUser
            }
        }
    }

    fun signOut() {
        auth.signOut()
        currentUser.value = null
    }
}
