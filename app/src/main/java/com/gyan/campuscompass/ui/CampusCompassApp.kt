package com.gyan.campuscompass.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gyan.campuscompass.model.Post
import com.gyan.campuscompass.ui.components.FloatingPillNavigationBar
import com.gyan.campuscompass.ui.screens.CreatePostScreen
import com.gyan.campuscompass.ui.screens.MainFeedScreen
import com.gyan.campuscompass.ui.screens.ProfileScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampusCompassApp(
    posts: List<Post>,
    isLoggedIn: Boolean,
    onLoginClick: () -> Unit
) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    
    val sheetState = rememberModalBottomSheetState()
    var showSOSSheet by remember { mutableStateOf(false) }

    val navItems = listOf(
        NavigationItem("Home Feed", Icons.Default.Home, "feed"),
        NavigationItem("My Profile", Icons.Default.Person, "profile"),
        NavigationItem("Saved Posts", Icons.Default.Bookmark, "saved"),
        NavigationItem("Admin Panel", Icons.Default.AdminPanelSettings, "admin")
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp, horizontal = 16.dp)
                ) {
                    Text(
                        text = "Campus Compass",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                
                HorizontalDivider()
                Spacer(modifier = Modifier.height(8.dp))

                navItems.forEach { item ->
                    NavigationDrawerItem(
                        icon = { Icon(item.icon, contentDescription = null) },
                        label = { Text(item.name) },
                        selected = false, // We'll handle selection based on route if needed
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate(item.route) {
                                launchSingleTop = true
                            }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            NavHost(navController = navController, startDestination = "feed") {
                composable("feed") {
                    MainFeedScreen(
                        posts = posts,
                        isLoggedIn = isLoggedIn,
                        onLoginClick = onLoginClick,
                        onMenuClick = { scope.launch { drawerState.open() } },
                        onCreatePostClick = { navController.navigate("create_post") },
                        onSOSClick = { showSOSSheet = true }
                    )
                }
                
                composable("create_post") {
                    CreatePostScreen(onBackClick = { navController.popBackStack() })
                }

                composable("profile") {
                    ProfileScreen(onBackClick = { navController.popBackStack() })
                }

                composable("saved") {
                    // Placeholder for Saved Posts
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = { Text("Saved Posts") },
                                navigationIcon = {
                                    IconButton(onClick = { navController.popBackStack() }) {
                                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                                    }
                                }
                            )
                        }
                    ) { p -> Box(Modifier.padding(p).fillMaxSize(), contentAlignment = Alignment.Center) { Text("No saved posts yet.") } }
                }
            }

            // Floating Pill Navigation Bar (DFD 6.0)
            FloatingPillNavigationBar(
                selectedItem = "Home", // This should be dynamic based on navController backstack
                onItemSelected = { tab ->
                    when(tab) {
                        "Home" -> navController.navigate("feed") { popUpTo("feed") { inclusive = true } }
                        "Profile" -> navController.navigate("profile")
                        "Saved" -> navController.navigate("saved")
                    }
                },
                modifier = Modifier.align(Alignment.BottomCenter)
            )

            // SOS Bottom Sheet (DFD 5.0)
            if (showSOSSheet) {
                ModalBottomSheet(
                    onDismissRequest = { showSOSSheet = false },
                    sheetState = sheetState,
                    containerColor = MaterialTheme.colorScheme.surface,
                    dragHandle = { BottomSheetDefaults.DragHandle() }
                ) {
                    SOSSheetContent()
                }
            }
        }
    }
}

@Composable
fun SOSSheetContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .padding(bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Emergency Assistance",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Immediate help is one tap away.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(
            onClick = { /* D4: SOS Helplines - Dial 112 */ },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(Icons.Default.Phone, contentDescription = null)
            Spacer(modifier = Modifier.width(12.dp))
            Text("Call Campus Security", fontWeight = FontWeight.Bold)
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        OutlinedButton(
            onClick = { /* Dial 100 */ },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Call Local Police", color = MaterialTheme.colorScheme.secondary)
        }
    }
}

data class NavigationItem(
    val name: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val route: String
)
