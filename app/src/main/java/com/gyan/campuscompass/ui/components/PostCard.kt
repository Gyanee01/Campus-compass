package com.gyan.campuscompass.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.gyan.campuscompass.model.Post
import com.gyan.campuscompass.model.User
import kotlinx.coroutines.delay

@Composable
fun PostCard(
    post: Post,
    currentUserId: String = "",
    modifier: Modifier = Modifier,
    isBiteSized: Boolean = false,
    onUserClick: (User) -> Unit = {},
    onLikeClick: () -> Unit = {},
    onCommentClick: () -> Unit = {},
    onClick: () -> Unit = {}
) {
    val isLiked = post.likedBy.contains(currentUserId)
    
    // Animation state for like button
    var likeAnimate by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (likeAnimate) 1.3f else 1.0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "likeScale"
    )

    LaunchedEffect(likeAnimate) {
        if (likeAnimate) {
            delay(200)
            likeAnimate = false
        }
    }

    // Background Artwork System (Requirement 33)
    val coverBrushes = listOf(
        Brush.linearGradient(listOf(Color(0xFFE0F7FA), Color(0xFFB2EBF2))),
        Brush.linearGradient(listOf(Color(0xFFF3E5F5), Color(0xFFE1BEE7))),
        Brush.linearGradient(listOf(Color(0xFFE8F5E9), Color(0xFFC8E6C9))),
        Brush.linearGradient(listOf(Color(0xFFFFF3E0), Color(0xFFFFE0B2))),
        Brush.linearGradient(listOf(Color(0xFFFBE9E7), Color(0xFFFFCCBC))),
        Brush.linearGradient(listOf(Color(0xFFECEFF1), Color(0xFFCFD8DC)))
    )
    val currentBrush = coverBrushes[post.coverArtworkIndex % coverBrushes.size]

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Box(modifier = Modifier.background(currentBrush)) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                // Header
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onUserClick(post.author) }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surface),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = post.author.username,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = post.author.studentYear,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    }
                    
                    if (!isBiteSized && post.category.name != "OTHER") {
                        Surface(
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = post.category.name.replace("_", " "),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Body
                Text(
                    text = if (post.venueName.isNotEmpty()) "${post.venueName}, ${post.destinationCity}" else post.destinationCity,
                    style = if (isBiteSized) MaterialTheme.typography.titleMedium else MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                if (!isBiteSized) {
                    if (post.images.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        AsyncImage(
                            model = post.images.first().url,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(16.dp))
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Tags Row
                    if (post.tags.isNotEmpty()) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.padding(bottom = 12.dp)
                        ) {
                            post.tags.take(3).forEach { tag ->
                                Surface(
                                    color = Color.White.copy(alpha = 0.6f),
                                    shape = RoundedCornerShape(6.dp),
                                    border = androidx.compose.foundation.BorderStroke(0.5.dp, Color.Black.copy(alpha = 0.1f))
                                ) {
                                    Text(
                                        text = "#$tag",
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        style = MaterialTheme.typography.labelSmall,
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }
                    }

                    // Budget Summary
                    Surface(
                        color = Color.White.copy(alpha = 0.4f),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                CustomIcon(Icons.Default.Schedule, contentDescription = null, size = 16.dp, tint = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("${post.numberOfDays} Days", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                CustomIcon(Icons.Default.Payments, contentDescription = null, size = 16.dp, tint = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("₹${post.totalBudget.toInt()}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                } else {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "₹${post.totalBudget.toInt()} • ${post.numberOfDays} Days",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Footer
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { 
                                    likeAnimate = true
                                    onLikeClick() 
                                }
                                .padding(4.dp)
                        ) {
                            Icon(
                                imageVector = if (isLiked) Icons.Filled.ThumbUp else Icons.Outlined.ThumbUp,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(20.dp)
                                    .scale(scale),
                                tint = if (isLiked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            
                            AnimatedContent(
                                targetState = post.likesCount,
                                transitionSpec = {
                                    if (targetState > initialState) {
                                        slideInVertically { it } + fadeIn() togetherWith slideOutVertically { -it } + fadeOut()
                                    } else {
                                        slideInVertically { -it } + fadeIn() togetherWith slideOutVertically { it } + fadeOut()
                                    }.using(SizeTransform(clip = false))
                                },
                                label = "likeCountAnimation"
                            ) { count ->
                                Text(
                                    text = "$count",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isLiked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.width(16.dp))
                        
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { onCommentClick() }
                                .padding(4.dp)
                        ) {
                            Icon(imageVector = Icons.Outlined.ChatBubbleOutline, contentDescription = null, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("${post.commentsCount}", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                        }
                    }
                    
                    IconButton(onClick = { /* Share Action */ }, modifier = Modifier.size(32.dp)) {
                        Icon(imageVector = Icons.Outlined.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun CustomIcon(imageVector: androidx.compose.ui.graphics.vector.ImageVector, contentDescription: String?, size: androidx.compose.ui.unit.Dp, tint: Color) {
    Icon(imageVector = imageVector, contentDescription = contentDescription, modifier = Modifier.size(size), tint = tint)
}
