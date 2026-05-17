package com.gyan.campuscompass

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.gyan.campuscompass.model.AuthViewModel
import com.gyan.campuscompass.model.PostViewModel
import com.gyan.campuscompass.model.getMockPosts
import com.gyan.campuscompass.ui.CampusCompassApp
import com.gyan.campuscompass.ui.theme.CampusCompassTheme

class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()
    private val postViewModel: PostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CampusCompassTheme {
                CampusCompassApp(
                    authViewModel = authViewModel,
                    postViewModel = postViewModel
                )
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CampusCompassTheme {
        Greeting("Android")
    }
}