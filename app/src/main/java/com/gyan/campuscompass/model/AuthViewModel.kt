package com.gyan.campuscompass.model

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import com.gyan.campuscompass.Config

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Authenticated : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel : ViewModel() {
    private val auth by lazy { FirebaseAuth.getInstance() }
    private val db by lazy { FirebaseFirestore.getInstance() }

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    private val _userProfile = mutableStateOf<User?>(null)
    val userProfile: State<User?> = _userProfile

    private val _isGuest = mutableStateOf(false)
    val isGuest: State<Boolean> = _isGuest

    private val demoUsers = listOf(
        User("u1", "campus_rider", "rider.demo@campuscompass.app", isDemo = true),
        User("u2", "travel_senpai", "senpai.demo@campuscompass.app", isDemo = true),
        User("u3", "budget_nomad", "nomad.demo@campuscompass.app", isDemo = true),
        User("u4", "college_wanderer", "wanderer.demo@campuscompass.app", isDemo = true)
    )
    private val demoPasswords = listOf("Campus@123", "Compass@123", "Travel@123", "Explore@123")

    init {
        if (!Config.DEMO_MODE) {
            auth.addAuthStateListener { firebaseAuth ->
                viewModelScope.launch {
                    val user = firebaseAuth.currentUser
                    if (user != null) {
                        _isGuest.value = false
                        Log.d("AuthViewModel", "User authenticated: ${user.uid}")
                        fetchUserProfile(user.uid)
                    } else {
                        if (!_isGuest.value) {
                            _userProfile.value = null
                        }
                    }
                }
            }
            seedDemoUsers()
        } else {
            Log.d("AuthViewModel", "DEMO MODE ENABLED: Skipping Firebase initialization")
        }
    }

    private fun seedDemoUsers() {
        if (Config.DEMO_MODE) return
        viewModelScope.launch {
            val demoUsers = listOf(
                User("u1", "campus_rider", "rider.demo@campuscompass.app", isDemo = true),
                User("u2", "travel_senpai", "senpai.demo@campuscompass.app", isDemo = true),
                User("u3", "budget_nomad", "nomad.demo@campuscompass.app", isDemo = true),
                User("u4", "college_wanderer", "wanderer.demo@campuscompass.app", isDemo = true)
            )
            val demoPasswords = listOf("Campus@123", "Compass@123", "Travel@123", "Explore@123")

            demoUsers.forEachIndexed { index, user ->
                try {
                    // Try to sign in first
                    try {
                        auth.signInWithEmailAndPassword(user.email, demoPasswords[index]).await()
                        Log.d("AuthViewModel", "Demo user ${user.username} already exists.")
                    } catch (e: Exception) {
                        // If fails, create the user
                        Log.d("AuthViewModel", "Creating demo user ${user.username}...")
                        val result = auth.createUserWithEmailAndPassword(user.email, demoPasswords[index]).await()
                        val uid = result.user?.uid ?: return@forEachIndexed
                        val finalUser = user.copy(id = uid)
                        db.collection("users").document(uid).set(finalUser).await()
                        Log.d("AuthViewModel", "Demo user ${user.username} created successfully.")
                    }
                } catch (e: Exception) {
                    Log.e("AuthViewModel", "Error seeding demo user ${user.username}: ${e.message}")
                }
            }
            // Sign out after seeding if we signed in
            auth.signOut()
        }
    }

    fun continueAsGuest() {
        _isGuest.value = true
        _userProfile.value = User(id = "guest", username = "Guest User")
        _authState.value = AuthState.Authenticated
        Log.d("AuthViewModel", "Continuing as guest")
    }

    fun loginWithDemo(email: String, pass: String) {
        Log.d("AuthViewModel", "Logging in with demo: $email")
        login(email, pass)
    }

    private suspend fun fetchUserProfile(uid: String) {
        try {
            val doc = db.collection("users").document(uid).get().await()
            if (doc.exists()) {
                _userProfile.value = doc.toObject(User::class.java)
                _authState.value = AuthState.Authenticated
                Log.d("AuthViewModel", "Profile fetched for $uid: ${_userProfile.value?.username}")
            } else {
                Log.w("AuthViewModel", "Profile doc NOT FOUND for $uid")
                _authState.value = AuthState.Idle
            }
        } catch (e: Exception) {
            Log.e("AuthViewModel", "Error fetching profile for $uid: ${e.message}")
            _authState.value = AuthState.Error(e.message ?: "Failed to fetch profile")
        }
    }

    fun login(email: String, pass: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            if (Config.DEMO_MODE) {
                delay(1000) // Simulate network delay
                val demoIndex = demoUsers.indexOfFirst { it.email == email }
                if (demoIndex != -1 && demoPasswords[demoIndex] == pass) {
                    _userProfile.value = demoUsers[demoIndex]
                    _authState.value = AuthState.Authenticated
                    _isGuest.value = false
                    Log.d("AuthViewModel", "Demo Login successful for $email")
                } else {
                    _authState.value = AuthState.Error("Invalid demo credentials")
                    Log.e("AuthViewModel", "Demo Login failed for $email")
                }
                return@launch
            }
            try {
                auth.signInWithEmailAndPassword(email, pass).await()
                Log.d("AuthViewModel", "Login successful for $email")
                // AuthStateListener will trigger fetchUserProfile
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Login failed for $email: ${e.message}")
                _authState.value = AuthState.Error(e.message ?: "Login Failed")
            }
        }
    }

    fun signup(user: User, pass: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            if (Config.DEMO_MODE) {
                delay(1500)
                val newUser = user.copy(id = "local_${System.currentTimeMillis()}")
                _userProfile.value = newUser
                _authState.value = AuthState.Authenticated
                _isGuest.value = false
                Log.d("AuthViewModel", "Demo Signup successful for ${user.email}")
                return@launch
            }
            try {
                val result = auth.createUserWithEmailAndPassword(user.email, pass).await()
                val uid = result.user?.uid ?: throw Exception("Signup Failed")
                val finalUser = user.copy(id = uid)
                db.collection("users").document(uid).set(finalUser).await()
                _userProfile.value = finalUser
                _authState.value = AuthState.Authenticated
                Log.d("AuthViewModel", "Signup successful for ${user.email}")
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Signup failed for ${user.email}: ${e.message}")
                _authState.value = AuthState.Error(e.message ?: "Signup Failed")
            }
        }
    }

    fun checkUsername(username: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            if (Config.DEMO_MODE) {
                val exists = demoUsers.any { it.username == username }
                onResult(!exists)
                return@launch
            }
            val query = db.collection("users").whereEqualTo("username", username).get().await()
            onResult(query.isEmpty)
        }
    }

    fun signOut() {
        if (!Config.DEMO_MODE) {
            auth.signOut()
        }
        _userProfile.value = null
        _authState.value = AuthState.Idle
        _isGuest.value = false
    }
}
