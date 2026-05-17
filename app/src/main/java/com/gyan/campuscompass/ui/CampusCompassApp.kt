package com.gyan.campuscompass.ui

import androidx.compose.animation.*
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
import androidx.compose.ui.res.painterResource
import com.gyan.campuscompass.R
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import com.gyan.campuscompass.model.AuthViewModel
import com.gyan.campuscompass.model.PostViewModel
import com.gyan.campuscompass.model.User
import com.gyan.campuscompass.ui.components.FloatingPillNavigationBar
import com.gyan.campuscompass.ui.components.AuthPromptBottomSheet
import com.gyan.campuscompass.ui.screens.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampusCompassApp(
    postViewModel: PostViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    
    val pagerState = rememberPagerState(initialPage = 1) { 3 }
    
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    val userProfile by authViewModel.userProfile
    val isGuest by authViewModel.isGuest
    val isLoggedIn = userProfile != null

    var showAuthPrompt by remember { mutableStateOf(false) }

    // Bottom nav visibility
    val shouldShowBottomNav = when {
        currentRoute == "main_pager" -> true
        currentRoute == "colleges" -> true
        else -> false
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp, horizontal = 16.dp)
                ) {
                    Column {
                        androidx.compose.foundation.Image(
                            painter = painterResource(id = R.drawable.app_logo),
                            contentDescription = "App Logo",
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Campus Compass",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                        if (isLoggedIn) {
                            Text(
                                text = "Hi, ${userProfile?.username}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                
                HorizontalDivider()
                Spacer(modifier = Modifier.height(8.dp))

                val drawerItems = mutableListOf(
                    NavigationItem("Home Feed", Icons.Default.Home, "main_pager"),
                    NavigationItem("Colleges", Icons.Default.School, "colleges")
                )
                
                drawerItems.forEach { item ->
                    NavigationDrawerItem(
                        icon = { Icon(item.icon, contentDescription = null) },
                        label = { Text(item.name) },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate(item.route) {
                                launchSingleTop = true
                            }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                if (isLoggedIn) {
                    NavigationDrawerItem(
                        icon = { Icon(Icons.Default.Logout, contentDescription = null) },
                        label = { Text("Logout") },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            authViewModel.signOut()
                            navController.navigate("login") {
                                popUpTo(0)
                            }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            NavHost(
                navController = navController, 
                startDestination = if (isLoggedIn) "main_pager" else "login",
                enterTransition = { fadeIn() + slideInHorizontally { it } },
                exitTransition = { fadeOut() + slideOutHorizontally { -it } },
                popEnterTransition = { fadeIn() + slideInHorizontally { -it } },
                popExitTransition = { fadeOut() + slideOutHorizontally { it } }
            ) {
                composable("login") {
                    LoginScreen(
                        viewModel = authViewModel,
                        onNavigateToSignup = { navController.navigate("signup") },
                        onLoginSuccess = {
                            navController.navigate("main_pager") {
                                popUpTo("login") { inclusive = true }
                            }
                        }
                    )
                }
                
                composable("signup") {
                    SignupScreen(
                        viewModel = authViewModel,
                        onNavigateToLogin = { navController.popBackStack() },
                        onSignupSuccess = {
                            navController.navigate("main_pager") {
                                popUpTo("login") { inclusive = true }
                            }
                        }
                    )
                }

                composable("main_pager") {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxSize()
                    ) { page ->
                        when (page) {
                            0 -> SavedPostsScreen(
                                onBackClick = { scope.launch { pagerState.animateScrollToPage(1) } },
                                onUserClick = { user: User ->
                                    navController.navigate("profile/${user.id}")
                                },
                                onPostClick = { post: com.gyan.campuscompass.model.Post ->
                                    navController.navigate("comments/${post.id}")
                                }
                            )
                            1 -> MainFeedScreen(
                                viewModel = postViewModel,
                                isLoggedIn = isLoggedIn && !isGuest,
                                onLoginClick = { 
                                    navController.navigate("login") {
                                        // No popUpTo(0) here to allow back navigation
                                    }
                                },
                                onMenuClick = { scope.launch { drawerState.open() } },
                                onCreatePostClick = { 
                                    if (isGuest) showAuthPrompt = true
                                    else navController.navigate("create_post")
                                },
                                onUserClick = { user: User ->
                                    navController.navigate("profile/${user.id}")
                                },
                                onCommentClick = { post ->
                                    navController.navigate("comments/${post.id}")
                                },
                                onPostClick = { post ->
                                    navController.navigate("post_details/${post.id}")
                                },
                                onLikeClick = { postId ->
                                    if (isGuest) showAuthPrompt = true
                                    else postViewModel.toggleLike(postId, userProfile?.id ?: "guest")
                                }
                            )
                            2 -> ProfileScreen(
                                onBackClick = { scope.launch { pagerState.animateScrollToPage(1) } },
                                userId = userProfile?.id,
                                isOwnProfile = true
                            )
                        }
                    }
                }
                
                composable("create_post") {
                    CreatePostScreen(onBackClick = { navController.popBackStack() })
                }

                composable("profile/{userId}") { backStackEntry ->
                    val userId = backStackEntry.arguments?.getString("userId")
                    ProfileScreen(
                        onBackClick = { navController.popBackStack() },
                        userId = userId,
                        isOwnProfile = userId == userProfile?.id
                    )
                }
                
                composable("colleges") {
                    CollegesScreen(
                        onBackClick = {
                            if (navController.previousBackStackEntry != null) {
                                navController.popBackStack()
                            } else {
                                scope.launch { pagerState.animateScrollToPage(1) }
                            }
                        }
                    )
                }

                composable("comments/{postId}") { backStackEntry ->
                    val postId = backStackEntry.arguments?.getString("postId") ?: ""
                    CommentScreen(
                        postId = postId,
                        onBackStackClick = { navController.popBackStack() },
                        isGuest = isGuest,
                        onAuthRequired = { showAuthPrompt = true }
                    )
                }

                composable("post_details/{postId}") { backStackEntry ->
                    val postId = backStackEntry.arguments?.getString("postId") ?: ""
                    PostDetailsScreen(
                        postId = postId,
                        viewModel = postViewModel,
                        onBackClick = { navController.popBackStack() },
                        onUserClick = { userId ->
                            navController.navigate("profile/$userId")
                        },
                        onCommentClick = {
                            navController.navigate("comments/$postId")
                        },
                        isGuest = isGuest,
                        onAuthRequired = { showAuthPrompt = true }
                    )
                }
            }

            if (showAuthPrompt) {
                AuthPromptBottomSheet(
                    onDismiss = { showAuthPrompt = false },
                    onLoginClick = {
                        showAuthPrompt = false
                        navController.navigate("login") {
                            popUpTo(0)
                        }
                    },
                    onSignupClick = {
                        showAuthPrompt = false
                        navController.navigate("signup") {
                            popUpTo(0)
                        }
                    }
                )
            }

            // Animated Bottom Bar
            AnimatedVisibility(
                visible = shouldShowBottomNav,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                FloatingPillNavigationBar(
                    selectedItem = when(pagerState.currentPage) {
                        0 -> "Saved"
                        1 -> "Home"
                        2 -> "Profile"
                        else -> "Home"
                    },
                    onItemSelected = { tab: String ->
                        scope.launch {
                            when(tab) {
                                "Home" -> {
                                    if (navController.currentBackStackEntry?.destination?.route != "main_pager") {
                                        navController.navigate("main_pager") { popUpTo("main_pager") { inclusive = true } }
                                    }
                                    pagerState.animateScrollToPage(1)
                                }
                                "Saved" -> {
                                    if (navController.currentBackStackEntry?.destination?.route != "main_pager") {
                                        navController.navigate("main_pager")
                                    }
                                    pagerState.animateScrollToPage(0)
                                }
                                "Profile" -> {
                                    if (isGuest) {
                                        showAuthPrompt = true
                                    } else {
                                        if (navController.currentBackStackEntry?.destination?.route != "main_pager") {
                                            navController.navigate("main_pager")
                                        }
                                        scope.launch { pagerState.animateScrollToPage(2) }
                                    }
                                }
                                "Create" -> {
                                    if (!isGuest) navController.navigate("create_post")
                                    else showAuthPrompt = true
                                }
                                "Colleges" -> navController.navigate("colleges")
                            }
                        }
                    }
                )
            }
        }
    }
}

data class NavigationItem(
    val name: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val route: String
)
