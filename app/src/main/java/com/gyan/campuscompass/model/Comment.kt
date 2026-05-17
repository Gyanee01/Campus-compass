package com.gyan.campuscompass.model

data class Comment(
    val id: String = "",
    val postId: String = "",
    val author: User = User(),
    val content: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val isFlagged: Boolean = false,
    val isQuestion: Boolean = false,
    val parentId: String? = null,
    val replies: List<Comment> = emptyList()
)
