package com.gyan.campuscompass.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import com.gyan.campuscompass.R
import com.gyan.campuscompass.model.FeedSortOrder
import com.gyan.campuscompass.model.Post
import com.gyan.campuscompass.model.PostCategory
import com.gyan.campuscompass.model.PostViewModel
import com.gyan.campuscompass.ui.components.PostCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainFeedScreen(
    viewModel: PostViewModel,
    isLoggedIn: Boolean = false,
    onLoginClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    onCreatePostClick: () -> Unit = {},
    onUserClick: (com.gyan.campuscompass.model.User) -> Unit = {},
    onCommentClick: (Post) -> Unit = {},
    onPostClick: (Post) -> Unit = {},
    onLikeClick: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedTag by remember { mutableStateOf("All") }
    val tags = listOf("All") + com.gyan.campuscompass.model.postTags
    
    val posts = viewModel.posts
    val sortOrder by viewModel.sortOrder
    val quickTrips = viewModel.quickTrips

    val postsToShow = remember(posts, selectedTag) {
        if (selectedTag == "All") posts
        else posts.filter { it.tags.contains(selectedTag) || it.category.name.replace("_", " ").equals(selectedTag, ignoreCase = true) }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface
                ),
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.app_logo),
                            contentDescription = null,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            "Campus Compass",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onMenuClick) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu", tint = MaterialTheme.colorScheme.secondary)
                    }
                },
                actions = {
                    IconButton(onClick = { /* Notification Action */ }) {
                        Icon(Icons.Default.Notifications, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                    }
                    if (!isLoggedIn) {
                        TextButton(onClick = onLoginClick) {
                            Text("Log In", fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("G", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            Column(horizontalAlignment = Alignment.End) {
                // Create Post FAB (Requirement 5: Appears on Feed)
                FloatingActionButton(
                    onClick = onCreatePostClick,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.padding(bottom = 80.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Create Post")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // Search Bar (Requirement 12)
            Surface(
                onClick = { /* Search Action */ },
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "Where do you want to travel?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            }

            // Tag Filters (Requirement 30)
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(tags) { tag ->
                    val isSelected = selectedTag == tag
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedTag = tag },
                        label = { Text(tag) },
                        shape = RoundedCornerShape(12.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // QuickTrips (Requirement 8)
                if (quickTrips.isNotEmpty()) {
                    item {
                        Text(
                            "QuickTrips",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(quickTrips) { post ->
                                PostCard(
                                    post = post,
                                    isBiteSized = true,
                                    modifier = Modifier.width(200.dp),
                                    onClick = { viewModel.addToQuickTrips(post) },
                                    onUserClick = onUserClick
                                )
                            }
                        }
                    }
                }

                // Feed Section Header with Sort Toggle (Requirement 9)
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = when(sortOrder) {
                                FeedSortOrder.RECENT -> "Recent Experiences"
                                FeedSortOrder.MOST_LIKED -> "Most Liked Trips"
                                FeedSortOrder.MOST_COMMENTED -> "Top Discussed"
                            },
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        
                        IconButton(onClick = {
                            val nextOrder = FeedSortOrder.entries[(sortOrder.ordinal + 1) % FeedSortOrder.entries.size]
                            viewModel.setSortOrder(nextOrder)
                        }) {
                            Icon(
                                imageVector = Icons.Default.SwapVert,
                                contentDescription = "Toggle Sort",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                items(postsToShow) { post ->
                    PostCard(
                        post = post,
                        currentUserId = "", // Handled via callback for guest logic
                        onClick = { onPostClick(post) },
                        onUserClick = onUserClick,
                        onCommentClick = { onCommentClick(post) },
                        onLikeClick = { onLikeClick(post.id) }
                    )
                }
                
                item { Spacer(modifier = Modifier.height(100.dp)) }
            }
        }
    }
}
