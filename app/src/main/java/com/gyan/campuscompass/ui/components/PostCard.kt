package com.gyan.campuscompass.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.ThumbDown
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gyan.campuscompass.model.Post
import com.gyan.campuscompass.model.PostCategory
import com.gyan.campuscompass.model.User
import com.gyan.campuscompass.ui.theme.CampusCompassTheme

@Composable
fun PostCard(
    post: Post,
    modifier: Modifier = Modifier,
    isBiteSized: Boolean = false
) {
    val backgroundColor = if (isBiteSized) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer
    val contentColor = if (isBiteSized) Color.White else MaterialTheme.colorScheme.onPrimaryContainer
    val secondaryContentColor = if (isBiteSized) Color.White.copy(alpha = 0.7f) else MaterialTheme.colorScheme.secondary

    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = backgroundColor),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(if (isBiteSized) Color.White.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surface),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = contentColor,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = post.author.username,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = contentColor
                    )
                    Text(
                        text = post.author.studentYear,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Light,
                        color = secondaryContentColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Body
            Text(
                text = post.destinationCity,
                style = if (isBiteSized) MaterialTheme.typography.titleMedium else MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold,
                color = contentColor
            )

            if (!isBiteSized) {
                Spacer(modifier = Modifier.height(12.dp))
                
                // Segmented block for Budget/Transport (Itinerary style)
                Surface(
                    color = Color.White.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Transport", style = MaterialTheme.typography.labelSmall, color = secondaryContentColor)
                            Text(post.transportMode, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Budget", style = MaterialTheme.typography.labelSmall, color = secondaryContentColor)
                            Text("₹${post.collegeAllowance?.toInt() ?: "Self"}", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = post.travelTips,
                    style = MaterialTheme.typography.bodyMedium,
                    color = contentColor.copy(alpha = 0.8f),
                    maxLines = 3
                )
            } else {
                Text(
                    text = "₹${post.collegeAllowance?.toInt() ?: "Free"}",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = contentColor
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
                    Icon(imageVector = Icons.Outlined.ThumbUp, contentDescription = null, modifier = Modifier.size(18.dp), tint = contentColor)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("${post.likesCount}", style = MaterialTheme.typography.labelSmall, color = contentColor)
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    Icon(imageVector = Icons.Outlined.ChatBubbleOutline, contentDescription = null, modifier = Modifier.size(18.dp), tint = contentColor)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("${post.commentsCount}", style = MaterialTheme.typography.labelSmall, color = contentColor)
                }
                
                Icon(imageVector = Icons.Outlined.Share, contentDescription = null, modifier = Modifier.size(18.dp), tint = contentColor)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PostCardPreview() {
    val mockPost = Post(
        author = User("", "Rahul", "3rd Year, IT"),
        category = PostCategory.EDUCATIONAL,
        destinationCity = "Bangalore",
        transportMode = "Train - Sleeper",
        accommodationDetails = "Provided by organizers",
        travelTips = "Carry an extension board! Make sure to register early for the hackathon.",
        collegeAllowance = 1500.0,
        likesCount = 45,
        dislikesCount = 2,
        commentsCount = 12
    )
    CampusCompassTheme {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            PostCard(post = mockPost)
            PostCard(post = mockPost, isBiteSized = true)
        }
    }
}
