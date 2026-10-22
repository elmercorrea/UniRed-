package com.example.unired

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.unired.ui.theme.UniRedTheme

enum class AppScreen {
    Splash,
    Welcome,
    Login,
    Register,
    ForgotPassword,
    Home,
    Configuracion,
    CrearForo
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
    val context = LocalContext.current
    // Creamos la conexión con SharedPreferences para guardar la sesión
    val sharedPreferences = remember {
        context.getSharedPreferences("UniRedPrefs", Context.MODE_PRIVATE)
    }

    var currentScreen by remember { mutableStateOf(AppScreen.Splash) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) {
        when (currentScreen) {
            AppScreen.Splash -> {
                SplashScreen(onTimeout = {
                    // Verificamos si hay una sesión guardada
                    val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
                    // Decidimos a qué pantalla ir
                    currentScreen = if (isLoggedIn) AppScreen.Home else AppScreen.Welcome
                })
            }
            AppScreen.Welcome -> {
                WelcomeScreen(onNavigateToLogin = { currentScreen = AppScreen.Login })
            }
            AppScreen.Login -> {
                LoginScreen(
                    onBackClick = { currentScreen = AppScreen.Welcome },
                    onForgotPasswordClick = { currentScreen = AppScreen.ForgotPassword },
                    onLoginSuccess = {
                        // Guardamos la sesión como iniciada
                        sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()
                        currentScreen = AppScreen.Home
                    },
                    onCreateAccountClick = { currentScreen = AppScreen.Register }
                )
            }
            AppScreen.Register -> {
                RegisterScreen(
                    onBackClick = { currentScreen = AppScreen.Login },
                    onRegisterSuccess = {
                        // Si el registro también inicia sesión automáticamente
                        sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()
                        currentScreen = AppScreen.Home
                    },
                    onLoginClick = { currentScreen = AppScreen.Login }
                )
            }
            AppScreen.ForgotPassword -> {
                ForgotPasswordScreen(
                    onBackClick = { currentScreen = AppScreen.Login }
                )
            }
            AppScreen.Home -> {
                HomeScreen(
                    onSettingsClick = { currentScreen = AppScreen.Configuracion },
                    onCreatePostClick = { currentScreen = AppScreen.CrearForo },
                    onAddStoryClick = { /* Acción para historia */ }
                )
            }
            AppScreen.Configuracion -> {
                ConfiguracionScreen(
                    onLogoutClick = {
                        // Borramos la sesión al salir
                        sharedPreferences.edit().putBoolean("isLoggedIn", false).apply()
                        currentScreen = AppScreen.Welcome
                    },
                    onHomeClick = { currentScreen = AppScreen.Home }
                )
            }
            AppScreen.CrearForo -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Pantalla para crear foro (En construcción)")
                        Button(onClick = { currentScreen = AppScreen.Home }) {
                            Text("Volver al Inicio")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainContentPreview() {
    UniRedTheme {
        MainContent()
    }
}
