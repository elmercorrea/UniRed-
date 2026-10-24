package com.example.unired

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.unired.ui.theme.UniRedTheme

@Composable
fun AyudaSoporteScreen(
    onContactarSoporteClick: () -> Unit = {},
    onVolverClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0A0B14))
                .statusBarsPadding()
                .height(60.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "UniRed+",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .clickable { onHomeClick() }
            )
            Spacer(modifier = Modifier.weight(1f))

            IconButton(onClick = onNotificationsClick) {
                Icon(imageVector = Icons.Default.Notifications, contentDescription = "Notificaciones", tint = Color.White)
            }
            IconButton(onClick = onProfileClick) {
                Icon(imageVector = Icons.Default.Person, contentDescription = "Perfil", tint = Color.White)
            }
            IconButton(onClick = onSettingsClick) {
                Icon(imageVector = Icons.Default.Settings, contentDescription = "Configuración", tint = Color.White)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Ayuda Y soporte",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                textAlign = TextAlign.Start
            )

            Text(
                text = "¿Cómo funciona UniRed+?\nEsta red social permite conectar a la comunidad académica mediante publicaciones, historias y chats directos. Si tienes problemas con tu cuenta, publicaciones bloqueadas o acceso al sistema, contacta a soporte técnico en soporte@unired.edu.co.",
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 22.sp,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(36.dp))

            Image(
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = "Logo UniRed+",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(130.dp)
                    .clip(RoundedCornerShape(26.dp))
            )

            Spacer(modifier = Modifier.height(36.dp))

            OutlinedButton(
                onClick = onContactarSoporteClick,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .width(220.dp)
                    .height(48.dp)
            ) {
                Text(
                    text = "Contactar Soporte",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = onVolverClick,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .width(130.dp)
                    .height(44.dp)
            ) {
                Text(
                    text = "Volver",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AyudaSoporteScreenPreview() {
    UniRedTheme {
        AyudaSoporteScreen()
    }
}
