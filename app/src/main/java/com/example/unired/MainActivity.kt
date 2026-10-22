package com.example.unired

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.unired.ui.theme.UniRedTheme

enum class AppScreen {
    Splash,
    Welcome,
    Login,
    ForgotPassword,
    Home
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UniRedTheme {
                MainContent()
            }
        }
    }
}

@Composable
fun MainContent() {
    var currentScreen by remember { mutableStateOf(AppScreen.Splash) }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (currentScreen) {
                AppScreen.Splash -> {
                    SplashScreen(onTimeout = { currentScreen = AppScreen.Welcome })
                }
                AppScreen.Welcome -> {
                    WelcomeScreen(onNavigateToLogin = { currentScreen = AppScreen.Login })
                }
                AppScreen.Login -> {
                    LoginScreen(
                        onBackClick = { currentScreen = AppScreen.Welcome },
                        onForgotPasswordClick = { currentScreen = AppScreen.ForgotPassword },
                        onLoginSuccess = { currentScreen = AppScreen.Home }
                    )
                }
                AppScreen.ForgotPassword -> {
                    ForgotPasswordScreen(
                        onBackClick = { currentScreen = AppScreen.Login }
                    )
                }
                AppScreen.Home -> {
                    HomeScreen(
                        onLogout = { currentScreen = AppScreen.Welcome }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainContentPreview() {
    UniRedTheme {
        WelcomeScreen()
    }
}
