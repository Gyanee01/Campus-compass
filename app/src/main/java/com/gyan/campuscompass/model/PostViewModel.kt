package com.gyan.campuscompass.model

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.gyan.campuscompass.Config
import kotlinx.coroutines.launch

enum class FeedSortOrder {
    RECENT, MOST_LIKED, MOST_COMMENTED
}

class PostViewModel : ViewModel() {
    private val db by lazy { FirebaseFirestore.getInstance() }
    private val _posts = mutableStateListOf<Post>()
    val posts: List<Post> = _posts

    private val _sortOrder = mutableStateOf(FeedSortOrder.RECENT)
    val sortOrder: State<FeedSortOrder> = _sortOrder

    private val _quickTrips = mutableStateListOf<Post>()
    val quickTrips: List<Post> = _quickTrips

    init {
        fetchPosts()
    }

    fun setSortOrder(order: FeedSortOrder) {
        _sortOrder.value = order
        fetchPosts()
    }

    fun addToQuickTrips(post: Post) {
        if (!_quickTrips.any { it.id == post.id }) {
            _quickTrips.add(0, post)
            if (_quickTrips.size > 5) _quickTrips.removeAt(_quickTrips.lastIndex)
        }
    }

    fun fetchPosts() {
        if (Config.DEMO_MODE) {
            _posts.clear()
            val mocks = getMockPosts()
            val sorted = when (_sortOrder.value) {
                FeedSortOrder.RECENT -> mocks.sortedByDescending { it.timestamp }
                FeedSortOrder.MOST_LIKED -> mocks.sortedByDescending { it.likesCount }
                FeedSortOrder.MOST_COMMENTED -> mocks.sortedByDescending { it.commentsCount }
            }
            _posts.addAll(sorted)
            return
        }
        val query = when (_sortOrder.value) {
            FeedSortOrder.RECENT -> db.collection("posts").orderBy("timestamp", Query.Direction.DESCENDING)
            FeedSortOrder.MOST_LIKED -> db.collection("posts").orderBy("likesCount", Query.Direction.DESCENDING)
            FeedSortOrder.MOST_COMMENTED -> db.collection("posts").orderBy("commentsCount", Query.Direction.DESCENDING)
        }

        query.addSnapshotListener { value, error ->
            if (error != null) return@addSnapshotListener
            if (value != null) {
                _posts.clear()
                for (doc in value.documents) {
                    try {
                        val post = doc.toObject(Post::class.java)
                        if (post != null) _posts.add(post.copy(id = doc.id))
                    } catch (e: Exception) {
                        // Mapping error
                    }
                }
                if (_posts.isEmpty() && _sortOrder.value == FeedSortOrder.RECENT) {
                    _posts.addAll(getMockPosts())
                }
            }
        }
    }

    fun toggleLike(postId: String, userId: String) {
        val post = _posts.find { it.id == postId } ?: return
        val isLiked = post.likedBy.contains(userId)

        // Optimistic UI update
        val updatedPost = if (isLiked) {
            post.copy(
                likesCount = post.likesCount - 1,
                likedBy = post.likedBy - userId
            )
        } else {
            post.copy(
                likesCount = post.likesCount + 1,
                likedBy = post.likedBy + userId
            )
        }
        
        val index = _posts.indexOf(post)
        if (index != -1) {
            _posts[index] = updatedPost
        }

        if (Config.DEMO_MODE) return

        // Backend update
        viewModelScope.launch {
            val postRef = db.collection("posts").document(postId)
            if (isLiked) {
                postRef.update(
                    "likesCount", FieldValue.increment(-1),
                    "likedBy", FieldValue.arrayRemove(userId)
                )
            } else {
                postRef.update(
                    "likesCount", FieldValue.increment(1),
                    "likedBy", FieldValue.arrayUnion(userId)
                )
            }
        }
    }
}
