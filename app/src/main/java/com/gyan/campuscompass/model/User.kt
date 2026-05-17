package com.gyan.campuscompass.model

data class User(
    val id: String = "",
    val username: String = "",
    val email: String = "",
    val collegeName: String = "",
    val branch: String = "",
    val studentYear: String = "",
    val profilePicUrl: String? = null,
    val postsCount: Int = 0,
    val likesReceived: Int = 0,
    val isDemo: Boolean = false
)
