package com.gyan.campuscompass.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.firebase.firestore.FirebaseFirestore
import com.gyan.campuscompass.Config
import com.gyan.campuscompass.model.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CreatePostScreen(
    onBackClick: () -> Unit,
    currentUser: User = User("u1", "rahul_it", "3rd Year, IT")
) {
    var cityName by remember { mutableStateOf("") }
    var stateName by remember { mutableStateOf("") }
    var venueName by remember { mutableStateOf("") }
    var accommodationName by remember { mutableStateOf("") }
    var accommodationCost by remember { mutableStateOf("") }
    var foodCost by remember { mutableStateOf("") }
    var transportMode by remember { mutableStateOf("Train") }
    var transportCost by remember { mutableStateOf("") }
    var travelTips by remember { mutableStateOf("") }
    var travelBlog by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(PostCategory.EDUCATIONAL) }

    // New Fields (Requirement 29)
    var numberOfDays by remember { mutableStateOf("1") }
    var bestTimeToVisit by remember { mutableStateOf("") }
    var companionType by remember { mutableStateOf(TravelCompanionType.FRIENDS) }
    var comfortLevel by remember { mutableStateOf(ComfortLevel.MODERATE) }
    var crowdLevel by remember { mutableStateOf(CrowdLevel.MODERATE) }
    var safetyRating by remember { mutableFloatStateOf(4f) }
    var internetRating by remember { mutableFloatStateOf(4f) }
    var foodRating by remember { mutableFloatStateOf(4f) }
    
    // SOS Fields (Requirement 82)
    var emergencyContactName by remember { mutableStateOf("") }
    var emergencyContactNumber by remember { mutableStateOf("") }
    var nearbyHospital by remember { mutableStateOf("") }
    var nearbyPoliceStation by remember { mutableStateOf("") }
    var emergencyNotes by remember { mutableStateOf("") }
    var safetyInstructions by remember { mutableStateOf("") }
    var emergencyTransport by remember { mutableStateOf("") }
    var safeAccommodationNotes by remember { mutableStateOf("") }
    
    // Tags (Requirement 30)
    var selectedTags by remember { mutableStateOf(setOf<String>()) }

    var selectedImages by remember { mutableStateOf(listOf<PostImage>()) }
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = 20),
        onResult = { uris ->
            val newImages = uris.map { PostImage(url = it.toString(), caption = "") }
            selectedImages = (selectedImages + newImages).take(20)
        }
    )

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Create Trip Post", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.Close, contentDescription = "Cancel")
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            if (cityName.isBlank() || venueName.isBlank()) return@TextButton
                            
                            val post = Post(
                                author = currentUser,
                                category = category,
                                destinationCity = cityName,
                                destinationState = stateName,
                                venueName = venueName,
                                transportMode = transportMode,
                                transportCost = transportCost.toDoubleOrNull() ?: 0.0,
                                accommodationName = accommodationName,
                                accommodationCost = accommodationCost.toDoubleOrNull() ?: 0.0,
                                foodCost = foodCost.toDoubleOrNull() ?: 0.0,
                                travelTips = travelTips,
                                travelBlog = travelBlog,
                                images = selectedImages,
                                numberOfDays = numberOfDays.toIntOrNull() ?: 1,
                                bestTimeToVisit = bestTimeToVisit,
                                travelCompanion = companionType,
                                comfortLevel = comfortLevel,
                                crowdLevel = crowdLevel,
                                safetyRating = safetyRating.toInt(),
                                internetAvailability = internetRating.toInt(),
                                foodQualityRating = foodRating.toInt(),
                                tags = selectedTags.toList(),
                                coverArtworkIndex = (0..5).random(),
                                timestamp = System.currentTimeMillis(),
                                emergencyContactName = emergencyContactName,
                                emergencyContactNumber = emergencyContactNumber,
                                nearbyHospital = nearbyHospital,
                                nearbyPoliceStation = nearbyPoliceStation,
                                emergencyNotes = emergencyNotes,
                                safetyInstructions = safetyInstructions,
                                emergencyTransport = emergencyTransport,
                                safeAccommodationNotes = safeAccommodationNotes
                            )
                            
                            if (!Config.DEMO_MODE) {
                                FirebaseFirestore.getInstance().collection("posts").add(post)
                            } else {
                                // In demo mode, we just simulate success
                                println("DEMO MODE: Post created locally (simulated)")
                            }
                            onBackClick()
                        },
                        enabled = cityName.isNotBlank() && venueName.isNotBlank()
                    ) {
                        Text("Post", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Section: Basic Info
            CreatePostSection(title = "General Information", icon = Icons.Default.LocationOn) {
                OutlinedTextField(
                    value = venueName,
                    onValueChange = { venueName = it },
                    label = { Text("Place / College / Venue Name") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                )
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = cityName,
                        onValueChange = { cityName = it },
                        label = { Text("City") },
                        modifier = Modifier.weight(1f),
                        shape = MaterialTheme.shapes.medium
                    )
                    OutlinedTextField(
                        value = stateName,
                        onValueChange = { stateName = it },
                        label = { Text("State") },
                        modifier = Modifier.weight(1f),
                        shape = MaterialTheme.shapes.medium
                    )
                }
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = numberOfDays,
                        onValueChange = { if (it.all { char -> char.isDigit() }) numberOfDays = it },
                        label = { Text("Duration (Days)") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = MaterialTheme.shapes.medium
                    )
                    OutlinedTextField(
                        value = bestTimeToVisit,
                        onValueChange = { bestTimeToVisit = it },
                        label = { Text("Best Time to Visit") },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("e.g. Oct - Mar") },
                        shape = MaterialTheme.shapes.medium
                    )
                }
            }

            // Section: Trip Characteristics (Requirement 29)
            CreatePostSection(title = "Trip Details", icon = Icons.Default.Tune) {
                Text("Companion Type", style = MaterialTheme.typography.labelLarge)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TravelCompanionType.entries.forEach { type ->
                        FilterChip(
                            selected = companionType == type,
                            onClick = { companionType = type },
                            label = { Text(type.name.lowercase().replaceFirstChar { it.uppercase() }) }
                        )
                    }
                }

                Text("Comfort Level", style = MaterialTheme.typography.labelLarge)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ComfortLevel.entries.forEach { level ->
                        FilterChip(
                            selected = comfortLevel == level,
                            onClick = { comfortLevel = level },
                            label = { Text(level.name.lowercase().replaceFirstChar { it.uppercase() }) }
                        )
                    }
                }

                Text("Crowd Level", style = MaterialTheme.typography.labelLarge)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    CrowdLevel.entries.forEach { level ->
                        FilterChip(
                            selected = crowdLevel == level,
                            onClick = { crowdLevel = level },
                            label = { Text(level.name.lowercase().replaceFirstChar { it.uppercase() }) }
                        )
                    }
                }
            }
            
            // Section: Ratings
            CreatePostSection(title = "Ratings", icon = Icons.Default.Star) {
                RatingSlider(label = "Safety", value = safetyRating, onValueChange = { safetyRating = it })
                RatingSlider(label = "Internet/Network", value = internetRating, onValueChange = { internetRating = it })
                RatingSlider(label = "Food Quality", value = foodRating, onValueChange = { foodRating = it })
            }

            // Section: Tags (Requirement 30)
            CreatePostSection(title = "Smart Tags", icon = Icons.Default.LocalOffer) {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    postTags.forEach { tag ->
                        val isSelected = selectedTags.contains(tag)
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                selectedTags = if (isSelected) selectedTags - tag else selectedTags + tag
                            },
                            label = { Text(tag) },
                            leadingIcon = if (isSelected) {
                                { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
                            } else null
                        )
                    }
                }
            }

            // Section: Transportation
            CreatePostSection(title = "Transportation", icon = Icons.Default.DirectionsBus) {
                var transportExpanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = transportExpanded,
                    onExpandedChange = { transportExpanded = !transportExpanded }
                ) {
                    OutlinedTextField(
                        value = transportMode,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Mode of Transport") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = transportExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium
                    )
                    ExposedDropdownMenu(
                        expanded = transportExpanded,
                        onDismissRequest = { transportExpanded = false }
                    ) {
                        listOf("Train", "Bus", "Flight", "Car", "Bike").forEach { mode ->
                            DropdownMenuItem(
                                text = { Text(mode) },
                                onClick = {
                                    transportMode = mode
                                    transportExpanded = false
                                }
                            )
                        }
                    }
                }
                OutlinedTextField(
                    value = transportCost,
                    onValueChange = { if (it.all { char -> char.isDigit() }) transportCost = it },
                    label = { Text("Transportation Cost (₹)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = MaterialTheme.shapes.medium
                )
            }

            // Section: Stay & Food
            CreatePostSection(title = "Accommodation & Food", icon = Icons.Default.Hotel) {
                OutlinedTextField(
                    value = accommodationName,
                    onValueChange = { accommodationName = it },
                    label = { Text("Stay Name (e.g. Zostel, Hotel)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                )
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = accommodationCost,
                        onValueChange = { if (it.all { char -> char.isDigit() }) accommodationCost = it },
                        label = { Text("Stay Cost (₹)") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = MaterialTheme.shapes.medium
                    )
                    OutlinedTextField(
                        value = foodCost,
                        onValueChange = { if (it.all { char -> char.isDigit() }) foodCost = it },
                        label = { Text("Food Cost (₹)") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = MaterialTheme.shapes.medium
                    )
                }
            }

            // Section: Travel Blog
            CreatePostSection(title = "Your Story", icon = Icons.Default.EditNote) {
                OutlinedTextField(
                    value = travelBlog,
                    onValueChange = { travelBlog = it },
                    placeholder = { Text("Share your detailed story, experiences and hidden gems...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 150.dp),
                    shape = MaterialTheme.shapes.medium
                )
            }

            // Section: Images
            CreatePostSection(title = "Photos (${selectedImages.size}/20)", icon = Icons.Default.PhotoLibrary) {
                // ... (previous content remains same)
            }

            // Section: Emergency SOS Information (Requirement 82)
            CreatePostSection(title = "Emergency SOS Info (Optional)", icon = Icons.Default.Emergency) {
                OutlinedTextField(
                    value = emergencyContactName,
                    onValueChange = { emergencyContactName = it },
                    label = { Text("Emergency Contact Name") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                )
                OutlinedTextField(
                    value = emergencyContactNumber,
                    onValueChange = { emergencyContactNumber = it },
                    label = { Text("Emergency Contact Number") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    shape = MaterialTheme.shapes.medium
                )
                OutlinedTextField(
                    value = nearbyHospital,
                    onValueChange = { nearbyHospital = it },
                    label = { Text("Nearby Hospital Name") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                )
                OutlinedTextField(
                    value = nearbyPoliceStation,
                    onValueChange = { nearbyPoliceStation = it },
                    label = { Text("Nearby Police Station") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                )
                OutlinedTextField(
                    value = emergencyNotes,
                    onValueChange = { emergencyNotes = it },
                    label = { Text("Local Emergency Notes") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                )
                OutlinedTextField(
                    value = safetyInstructions,
                    onValueChange = { safetyInstructions = it },
                    label = { Text("Safety Instructions / Warnings") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                )
                OutlinedTextField(
                    value = emergencyTransport,
                    onValueChange = { emergencyTransport = it },
                    label = { Text("Emergency Transport Availability") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                )
                OutlinedTextField(
                    value = safeAccommodationNotes,
                    onValueChange = { safeAccommodationNotes = it },
                    label = { Text("Safe Accommodation Notes") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                )
            }

            // Budget Summary
            val totalBudget = (accommodationCost.toDoubleOrNull() ?: 0.0) + (foodCost.toDoubleOrNull() ?: 0.0) + (transportCost.toDoubleOrNull() ?: 0.0)
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                shape = RoundedCornerShape(24.dp)
            ) {
                Row(modifier = Modifier.padding(20.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        Text("Total Trip Budget", style = MaterialTheme.typography.labelMedium)
                        Text("₹${totalBudget.toInt()}", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold)
                    }
                    Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, modifier = Modifier.size(32.dp))
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun RatingSlider(label: String, value: Float, onValueChange: (Float) -> Unit) {
    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, style = MaterialTheme.typography.bodyMedium)
            Text("${value.toInt()}/5", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = 1f..5f,
            steps = 3
        )
    }
}

@Composable
fun CreatePostSection(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, content: @Composable ColumnScope.() -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
        content()
    }
}
