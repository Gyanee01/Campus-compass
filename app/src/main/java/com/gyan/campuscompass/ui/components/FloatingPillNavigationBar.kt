package com.gyan.campuscompass.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FloatingPillNavigationBar(
    selectedItem: String,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .height(64.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 12.dp,
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val items = listOf(
                NavigationTabData("Home", Icons.Default.Home, "feed"),
                NavigationTabData("Colleges", Icons.Default.School, "colleges"),
                NavigationTabData("Create", Icons.Default.Add, "create_post"),
                NavigationTabData("Saved", Icons.Default.Bookmark, "saved"),
                NavigationTabData("Profile", Icons.Default.Person, "profile")
            )

            items.forEach { data ->
                val isSelected = selectedItem == data.name
                val iconColor = if (isSelected) MaterialTheme.colorScheme.primary 
                               else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                
                IconButton(
                    onClick = { onItemSelected(data.name) },
                    modifier = Modifier.weight(1f)
                ) {
                    if (data.name == "Create") {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ) {
                            Icon(
                                imageVector = data.icon,
                                contentDescription = data.name,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    } else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = data.icon,
                                contentDescription = data.name,
                                tint = iconColor
                            )
                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .padding(top = 2.dp)
                                        .size(4.dp)
                                        .background(MaterialTheme.colorScheme.primary, CircleShape)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

data class NavigationTabData(val name: String, val icon: androidx.compose.ui.graphics.vector.ImageVector, val route: String)

