package com.gyan.campuscompass.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.gyan.campuscompass.model.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun PostDetailsScreen(
    postId: String,
    viewModel: PostViewModel,
    onBackClick: () -> Unit,
    onUserClick: (String) -> Unit,
    onCommentClick: () -> Unit,
    isGuest: Boolean = false,
    onAuthRequired: () -> Unit = {}
) {
    val post = viewModel.posts.find { it.id == postId }
    val sheetState = rememberModalBottomSheetState()
    var showSosSheet by remember { mutableStateOf(false) }

    if (post == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    // Cover Artwork System (Requirement 33)
    val coverBrushes = listOf(
        Brush.linearGradient(listOf(Color(0xFFE0F7FA), Color(0xFFB2EBF2))),
        Brush.linearGradient(listOf(Color(0xFFF3E5F5), Color(0xFFE1BEE7))),
        Brush.linearGradient(listOf(Color(0xFFE8F5E9), Color(0xFFC8E6C9))),
        Brush.linearGradient(listOf(Color(0xFFFFF3E0), Color(0xFFFFE0B2))),
        Brush.linearGradient(listOf(Color(0xFFFBE9E7), Color(0xFFFFCCBC))),
        Brush.linearGradient(listOf(Color(0xFFECEFF1), Color(0xFFCFD8DC)))
    )
    val currentBrush = coverBrushes[post.coverArtworkIndex % coverBrushes.size]

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Trip Experience", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showSosSheet = true }) {
                        Icon(
                            Icons.Default.Emergency,
                            contentDescription = "SOS Info",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                    IconButton(onClick = { /* Share */ }) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                    }
                    IconButton(onClick = { /* Save */ }) {
                        Icon(Icons.Default.BookmarkBorder, contentDescription = "Save")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        modifier = Modifier.background(currentBrush)
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // Image Gallery (Requirement 38)
                if (post.images.isNotEmpty()) {
                    val pagerState = rememberPagerState(pageCount = { post.images.size })
                    Box(modifier = Modifier.fillMaxWidth().height(350.dp).padding(16.dp)) {
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(24.dp))
                        ) { page ->
                            val image = post.images[page]
                            Box(modifier = Modifier.fillMaxSize()) {
                                AsyncImage(
                                    model = image.url,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize(),
                                )
                                if (image.caption.isNotEmpty()) {
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.BottomStart)
                                            .fillMaxWidth()
                                            .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))))
                                            .padding(20.dp).padding(top = 20.dp)
                                    ) {
                                        Text(
                                            text = image.caption,
                                            color = Color.White,
                                            style = MaterialTheme.typography.bodySmall,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            }
                        }
                        
                        // Pager Indicator
                        if (post.images.size > 1) {
                            Row(
                                Modifier.wrapContentHeight().fillMaxWidth().align(Alignment.BottomCenter).padding(bottom = 12.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                repeat(post.images.size) { iteration ->
                                    val color = if (pagerState.currentPage == iteration) Color.White else Color.White.copy(alpha = 0.5f)
                                    val width = if (pagerState.currentPage == iteration) 18.dp else 6.dp
                                    Box(
                                        modifier = Modifier.padding(3.dp).clip(CircleShape).background(color).height(6.dp).width(width)
                                    )
                                }
                            }
                        }
                    }
                }

                Column(modifier = Modifier.padding(20.dp)) {
                    // Tags Row (Requirement 30)
                    if (post.tags.isNotEmpty()) {
                        FlowRow(
                            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            post.tags.forEach { tag ->
                                Surface(
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(8.dp),
                                    border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                                ) {
                                    Text(
                                        text = "#$tag",
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    // Title and Location
                    Text(
                        text = post.venueName.ifEmpty { post.destinationCity },
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.ExtraBold,
                        lineHeight = 40.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${post.destinationCity}, ${post.destinationState}",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Trip Characteristics (Requirement 29)
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CharacteristicChip(Icons.Default.Groups, post.travelCompanion.name.lowercase().replaceFirstChar { it.uppercase() })
                        CharacteristicChip(Icons.Default.Star, "${post.safetyRating}/5 Safety")
                        CharacteristicChip(Icons.Default.Star, post.comfortLevel.name.lowercase().replaceFirstChar { it.uppercase() })
                    }

                    // Expense Breakdown
                    Text("Trip Stats", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        StatCard("Days", post.numberOfDays.toString(), Icons.Default.CalendarToday)
                        StatCard("Budget", "₹${post.totalBudget.toInt()}", Icons.Default.Payments)
                        StatCard("Crowd", post.crowdLevel.name.lowercase().replaceFirstChar { it.uppercase() }, Icons.Default.People)
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Travel Blog Section (Requirement 23)
                    if (post.travelBlog.isNotEmpty()) {
                        Text("The Adventure Blog", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = post.travelBlog,
                            style = MaterialTheme.typography.bodyLarge,
                            lineHeight = 28.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                    }

                    // Author Section
                    Surface(
                        color = Color.White.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(24.dp),
                        onClick = { onUserClick(post.author.id) }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(16.dp).fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier.size(48.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(post.author.username.take(1).uppercase(), color = Color.White, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(post.author.username, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Text(post.author.studentYear, style = MaterialTheme.typography.bodySmall)
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(Icons.Default.ChevronRight, contentDescription = null)
                        }
                    }

                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
            
            // Sticky Bottom Interaction Bar
            Box(modifier = Modifier.fillMaxSize().padding(20.dp), contentAlignment = Alignment.BottomCenter) {
                val currentUserId = if (isGuest) "guest" else "current_user_id"
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(24.dp),
                    shadowElevation = 8.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        val isLiked = post.likedBy.contains(currentUserId)
                        InteractionButton(
                            if (isLiked) Icons.Default.ThumbUp else Icons.Outlined.ThumbUp,
                            post.likesCount.toString()
                        ) {
                            if (isGuest) onAuthRequired()
                            else viewModel.toggleLike(post.id, currentUserId)
                        }
                        InteractionButton(Icons.Default.ChatBubbleOutline, post.commentsCount.toString()) { onCommentClick() }
                        InteractionButton(Icons.Default.Share, "Share") {}
                    }
                }
            }
        }
    }

    if (showSosSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSosSheet = false },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface,
            dragHandle = { BottomSheetDefaults.DragHandle() }
        ) {
            SosContent(post = post)
        }
    }
}

@Composable
fun SosContent(post: Post) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 24.dp)
        ) {
            Icon(
                Icons.Default.HealthAndSafety,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                "Emergency SOS Information",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error
            )
        }

        SosDetailItem(Icons.Default.Person, "Emergency Contact", post.emergencyContactName)
        SosDetailItem(Icons.Default.Phone, "Contact Number", post.emergencyContactNumber)
        SosDetailItem(Icons.Default.LocalHospital, "Nearby Hospital", post.nearbyHospital)
        SosDetailItem(Icons.Default.LocalPolice, "Police Station", post.nearbyPoliceStation)
        SosDetailItem(Icons.Default.DirectionsBus, "Emergency Transport", post.emergencyTransport)
        SosDetailItem(Icons.Default.Hotel, "Safe Accommodation", post.safeAccommodationNotes)
        
        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
        
        Text("Safety Instructions", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text(
            post.safetyInstructions.ifEmpty { "Follow local safety guidelines." },
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Important Notes", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text(
            post.emergencyNotes.ifEmpty { "No additional notes provided." },
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 8.dp)
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = { /* Could trigger a direct call here */ },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.Call, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Call Emergency Contact")
        }
        
        Spacer(modifier = Modifier.height(48.dp))
    }
}

@Composable
fun SosDetailItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    if (value.isEmpty()) return
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.secondary)
            Text(value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun CharacteristicChip(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Surface(
        color = Color.White.copy(alpha = 0.5f),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(6.dp))
            Text(text, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun StatCard(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Surface(
        color = Color.White.copy(alpha = 0.4f),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.width(100.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp), tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, fontWeight = FontWeight.ExtraBold, style = MaterialTheme.typography.bodyMedium)
            Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
        }
    }
}

@Composable
fun InteractionButton(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable { onClick() }
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp), tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(8.dp))
        Text(label, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
    }
}
