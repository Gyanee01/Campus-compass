package com.gyan.campuscompass.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gyan.campuscompass.model.Comment
import com.gyan.campuscompass.model.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentScreen(
    postId: String,
    onBackStackClick: () -> Unit,
    isGuest: Boolean = false,
    onAuthRequired: () -> Unit = {}
) {
    var commentText by remember { mutableStateOf("") }
    
    // Mock data for comments
    val comments = remember {
        mutableStateListOf(
            Comment(
                id = "c1",
                author = User(username = "alex_j", studentYear = "2nd Year"),
                content = "Great trip! How was the food there?",
                replies = listOf(
                    Comment(
                        id = "r1",
                        author = User(username = "rahul_it", studentYear = "3rd Year"),
                        content = "The canteen food was decent and affordable."
                    )
                )
            ),
            Comment(
                id = "c2",
                author = User(username = "sneha_v", studentYear = "1st Year"),
                content = "Is the registration still open?",
                isQuestion = true
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Comments", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackStackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            // Sticky comment input field above keyboard (Requirement 36)
            Surface(
                tonalElevation = 8.dp,
                shadowElevation = 8.dp,
                modifier = Modifier.imePadding() // Keyboard-aware
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = commentText,
                        onValueChange = { commentText = it },
                        placeholder = { Text("Add a comment...") },
                        modifier = Modifier.weight(1f),
                        enabled = !isGuest,
                        shape = RoundedCornerShape(24.dp),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        ),
                        maxLines = 4
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    FloatingActionButton(
                        onClick = {
                            if (isGuest) {
                                onAuthRequired()
                            } else if (commentText.isNotBlank()) {
                                comments.add(
                                    Comment(
                                        id = System.currentTimeMillis().toString(),
                                        author = User(username = "You", studentYear = "Me"),
                                        content = commentText
                                    )
                                )
                                commentText = ""
                            }
                        },
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White,
                        shape = CircleShape,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send", modifier = Modifier.size(20.dp))
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp) // Better spacing
        ) {
            items(comments, key = { it.id }) { comment ->
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn() + expandVertically()
                ) {
                    CommentItem(comment = comment)
                }
            }
        }
    }
}

@Composable
fun CommentItem(comment: Comment, isReply: Boolean = false) {
    var isFlagged by remember { mutableStateOf(comment.isFlagged) }

    // Highlight raised/question comments visually (Requirement 36)
    val containerColor = if (comment.isQuestion) {
        MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
    } else {
        Color.Transparent
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(containerColor)
            .padding(if (comment.isQuestion) 8.dp else 0.dp)
            .padding(start = if (isReply) 40.dp else 0.dp)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Box(
                modifier = Modifier
                    .size(if (isReply) 28.dp else 36.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(if (isReply) 16.dp else 20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = comment.author.username,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                        if (comment.isQuestion) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                color = MaterialTheme.colorScheme.secondary,
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.AutoMirrored.Filled.HelpOutline, contentDescription = null, modifier = Modifier.size(10.dp), tint = Color.White)
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text("QUESTION", style = MaterialTheme.typography.labelSmall, color = Color.White, fontSize = 8.sp)
                                }
                            }
                        }
                    }
                    IconButton(
                        onClick = { isFlagged = !isFlagged },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = if (isFlagged) Icons.Default.Flag else Icons.Outlined.Flag,
                            contentDescription = "Flag",
                            tint = if (isFlagged) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                Text(
                    text = comment.content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text(
                        text = "Reply",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Like",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
        
        if (comment.replies.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            comment.replies.forEach { reply ->
                CommentItem(comment = reply, isReply = true)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}
