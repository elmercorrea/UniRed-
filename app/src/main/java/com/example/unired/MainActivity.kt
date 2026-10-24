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
    Notificaciones,
    Perfil,
    Configuracion,
    Privacidad,
    CuentaPrivada,
    ConfigMensajes,
    ConfigActividad,
    AyudaSoporte,
    ContactarSoporte,
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
                    onNotificationsClick = { currentScreen = AppScreen.Notificaciones },
                    onProfileClick = { currentScreen = AppScreen.Perfil },
                    onSettingsClick = { currentScreen = AppScreen.Configuracion },
                    onCreatePostClick = { currentScreen = AppScreen.CrearForo },
                    onAddStoryClick = { /* Acción para historia */ }
                )
            }
            AppScreen.Notificaciones -> {
                NotificacionesScreen(
                    onHomeClick = { currentScreen = AppScreen.Home },
                    onProfileClick = { currentScreen = AppScreen.Perfil },
                    onSettingsClick = { currentScreen = AppScreen.Configuracion }
                )
            }
            AppScreen.Perfil -> {
                PerfilScreen(
                    onHomeClick = { currentScreen = AppScreen.Home },
                    onNotificationsClick = { currentScreen = AppScreen.Notificaciones },
                    onSettingsClick = { currentScreen = AppScreen.Configuracion },
                    onLogoutClick = {
                        sharedPreferences.edit().putBoolean("isLoggedIn", false).apply()
                        currentScreen = AppScreen.Welcome
                    }
                )
            }
            AppScreen.Configuracion -> {
                ConfiguracionScreen(
                    onNotificationsClick = { currentScreen = AppScreen.Notificaciones },
                    onProfileClick = { currentScreen = AppScreen.Perfil },
                    onPrivacidadClick = { currentScreen = AppScreen.Privacidad },
                    onAyudaSoporteClick = { currentScreen = AppScreen.AyudaSoporte },
                    onLogoutClick = {
                        // Borramos la sesión al salir
                        sharedPreferences.edit().putBoolean("isLoggedIn", false).apply()
                        currentScreen = AppScreen.Welcome
                    },
                    onHomeClick = { currentScreen = AppScreen.Home }
                )
            }
            AppScreen.Privacidad -> {
                PrivacidadScreen(
                    onHomeClick = { currentScreen = AppScreen.Home },
                    onNotificationsClick = { currentScreen = AppScreen.Notificaciones },
                    onProfileClick = { currentScreen = AppScreen.Perfil },
                    onSettingsClick = { currentScreen = AppScreen.Configuracion },
                    onCuentaPrivadaClick = { currentScreen = AppScreen.CuentaPrivada },
                    onConfigMensajesClick = { currentScreen = AppScreen.ConfigMensajes },
                    onConfigActividadClick = { currentScreen = AppScreen.ConfigActividad }
                )
            }
            AppScreen.CuentaPrivada -> {
                val esPrivada = sharedPreferences.getBoolean("cuenta_privada", false)
                OpcionSiNoScreen(
                    titulo = "Cuenta Privada",
                    descripcion = "Solo las personas que aceptes podrán seguirte y ver tus publicaciones.",
                    estadoInicialSi = esPrivada,
                    onCambioEstado = { sharedPreferences.edit().putBoolean("cuenta_privada", it).apply() },
                    onVolverClick = { currentScreen = AppScreen.Privacidad },
                    onHomeClick = { currentScreen = AppScreen.Home },
                    onNotificationsClick = { currentScreen = AppScreen.Notificaciones },
                    onProfileClick = { currentScreen = AppScreen.Perfil },
                    onSettingsClick = { currentScreen = AppScreen.Configuracion }
                )
            }
            AppScreen.ConfigMensajes -> {
                val permMensajes = sharedPreferences.getBoolean("config_mensajes", true)
                OpcionSiNoScreen(
                    titulo = "Configuracion de Mensajes",
                    descripcion = "Permite que otros estudiantes puedan enviarte mensajes.",
                    estadoInicialSi = permMensajes,
                    onCambioEstado = { sharedPreferences.edit().putBoolean("config_mensajes", it).apply() },
                    onVolverClick = { currentScreen = AppScreen.Privacidad },
                    onHomeClick = { currentScreen = AppScreen.Home },
                    onNotificationsClick = { currentScreen = AppScreen.Notificaciones },
                    onProfileClick = { currentScreen = AppScreen.Perfil },
                    onSettingsClick = { currentScreen = AppScreen.Configuracion }
                )
            }
            AppScreen.ConfigActividad -> {
                val verActividad = sharedPreferences.getBoolean("config_actividad", true)
                OpcionSiNoScreen(
                    titulo = "Configuracion de Actividad",
                    descripcion = "Permite que otros usuarios vean cuándo estás activo.",
                    estadoInicialSi = verActividad,
                    onCambioEstado = { sharedPreferences.edit().putBoolean("config_actividad", it).apply() },
                    onVolverClick = { currentScreen = AppScreen.Privacidad },
                    onHomeClick = { currentScreen = AppScreen.Home },
                    onNotificationsClick = { currentScreen = AppScreen.Notificaciones },
                    onProfileClick = { currentScreen = AppScreen.Perfil },
                    onSettingsClick = { currentScreen = AppScreen.Configuracion }
                )
            }
            AppScreen.AyudaSoporte -> {
                AyudaSoporteScreen(
                    onContactarSoporteClick = { currentScreen = AppScreen.ContactarSoporte },
                    onVolverClick = { currentScreen = AppScreen.Configuracion },
                    onHomeClick = { currentScreen = AppScreen.Home },
                    onNotificationsClick = { currentScreen = AppScreen.Notificaciones },
                    onProfileClick = { currentScreen = AppScreen.Perfil },
                    onSettingsClick = { currentScreen = AppScreen.Configuracion }
                )
            }
            AppScreen.ContactarSoporte -> {
                ContactarSoporteScreen(
                    onEnviarClick = { currentScreen = AppScreen.AyudaSoporte },
                    onCancelarClick = { currentScreen = AppScreen.AyudaSoporte },
                    onHomeClick = { currentScreen = AppScreen.Home },
                    onNotificationsClick = { currentScreen = AppScreen.Notificaciones },
                    onProfileClick = { currentScreen = AppScreen.Perfil },
                    onSettingsClick = { currentScreen = AppScreen.Configuracion }
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
