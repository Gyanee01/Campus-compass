package com.gyan.campuscompass.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gyan.campuscompass.model.AuthViewModel
import com.gyan.campuscompass.model.Post
import com.gyan.campuscompass.model.PostViewModel
import com.gyan.campuscompass.ui.components.PostCard
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBackClick: () -> Unit = {},
    userId: String? = null,
    isOwnProfile: Boolean = true,
    authViewModel: AuthViewModel = viewModel(),
    postViewModel: PostViewModel = viewModel()
) {
    val userProfile = if (isOwnProfile) authViewModel.userProfile.value else null
    // In a real app, we would fetch the profile for userId if not own profile
    
    val userPosts = postViewModel.posts.filter { it.author.id == (userId ?: userProfile?.id) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isOwnProfile) "My Profile" else "Traveler", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    if (!isOwnProfile) {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                },
                actions = {
                    if (isOwnProfile) {
                        IconButton(onClick = { /* Settings */ }) {
                            Icon(Icons.Default.Settings, contentDescription = "Settings")
                        }
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp, horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.size(56.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = userProfile?.username ?: "Traveler",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Text(
                        text = userProfile?.email ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Surface(
                        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "${userProfile?.studentYear ?: "N/A"} • ${userProfile?.collegeName ?: "Independent"}",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatItem(label = "Trips", value = "${userPosts.size}")
                        StatItem(label = "Likes", value = "${userProfile?.likesReceived ?: 0}")
                        StatItem(label = "Credits", value = "150") // Mock credits
                    }
                }
            }

            item {
                Text(
                    text = "Travel Memories",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )
            }

            if (userPosts.isEmpty()) {
                item {
                    Box(Modifier.fillMaxWidth().padding(48.dp), contentAlignment = Alignment.Center) {
                        Text("No posts shared yet.", color = MaterialTheme.colorScheme.secondary)
                    }
                }
            } else {
                items(userPosts) { post ->
                    PostCard(
                        post = post,
                        currentUserId = userProfile?.id ?: "",
                        onLikeClick = { postViewModel.toggleLike(post.id, userProfile?.id ?: "") },
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }
            
            item { Spacer(modifier = Modifier.height(100.dp)) }
        }
    }
}

@Composable
fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
        Text(text = label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
