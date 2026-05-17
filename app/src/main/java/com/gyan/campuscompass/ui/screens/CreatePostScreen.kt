package com.gyan.campuscompass.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import androidx.compose.ui.text.font.FontWeight
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostScreen(onBackClick: () -> Unit) {
    var destination by remember { mutableStateOf("") }
    var tips by remember { mutableStateOf("") }
    var transport by remember { mutableStateOf("") }
    
    val db = FirebaseFirestore.getInstance()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Share Your Trip", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = destination,
                onValueChange = { destination = it },
                label = { Text("Where did you go?") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = transport,
                onValueChange = { transport = it },
                label = { Text("How did you travel? (e.g. Bus, Train)") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = tips,
                onValueChange = { tips = it },
                label = { Text("Share your tips & experience") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 5
            )
            
            Button(
                onClick = { 
                    val post = hashMapOf(
                        "destinationCity" to destination,
                        "transportMode" to transport,
                        "travelTips" to tips,
                        "timestamp" to System.currentTimeMillis(),
                        "author" to hashMapOf(
                            "username" to "Student",
                            "studentYear" to "2nd Year"
                        ),
                        "likesCount" to 0,
                        "commentsCount" to 0
                    )
                    db.collection("posts").add(post)
                    onBackClick()
                },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Publish to Campus Feed")
            }
        }
    }
}
