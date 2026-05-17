package com.gyan.campuscompass.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SOSScreen(onBackClick: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Smart SOS System") },
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Emergency Helplines",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Red
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            val helplines = listOf(
                Helpline("Campus Security", "123-456-7890"),
                Helpline("Medical Emergency", "911"),
                Helpline("Women Helpline", "1091"),
                Helpline("Local Police", "100")
            )
            
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(helplines) { helpline ->
                    HelplineCard(helpline)
                }
            }
        }
    }
}

@Composable
fun HelplineCard(helpline: Helpline) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = helpline.name, style = MaterialTheme.typography.titleMedium)
                Text(text = helpline.number, style = MaterialTheme.typography.bodyMedium)
            }
            FilledIconButton(onClick = { /* Launch Dialler */ }) {
                Icon(Icons.Default.Phone, contentDescription = "Call")
            }
        }
    }
}

data class Helpline(val name: String, val number: String)
