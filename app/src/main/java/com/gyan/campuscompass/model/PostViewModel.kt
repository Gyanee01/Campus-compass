package com.gyan.campuscompass.model

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject

class PostViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    val posts = mutableStateListOf<Post>()

    init {
        fetchPosts()
    }

    private fun fetchPosts() {
        db.collection("posts")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                if (error != null) return@addSnapshotListener
                if (value != null) {
                    posts.clear()
                    for (doc in value.documents) {
                        try {
                            val post = doc.toObject(Post::class.java)
                            if (post != null) posts.add(post)
                        } catch (e: Exception) {
                            // Mapping error
                        }
                    }
                    if (posts.isEmpty()) {
                        posts.addAll(getMockPosts())
                    }
                }
            }
    }
}
