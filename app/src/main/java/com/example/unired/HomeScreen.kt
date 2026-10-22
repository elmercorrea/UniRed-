package com.example.unired

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.unired.ui.theme.UniRedTheme

@Composable
fun HomeScreen(
    onSettingsClick: () -> Unit = {},
    onCreatePostClick: () -> Unit = {},
    onAddStoryClick: () -> Unit = {}
) {
    Scaffold(
        containerColor = Color(0xFFF8F9FA),
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreatePostClick,
                containerColor = Color(0xFF6B52D9),
                contentColor = Color.White
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Crear nuevo foro")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                // Solo aplicamos el padding inferior para evitar que el botón flontante tape contenido,
                // ignorando el padding superior para que la barra negra cubra la barra de estado.
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
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
                    modifier = Modifier.padding(start = 16.dp)
                )
                Spacer(modifier = Modifier.weight(1f))

                IconButton(onClick = { /* Acción notificaciones */ }) {
                    Icon(imageVector = Icons.Default.Notifications, contentDescription = "Notificaciones", tint = Color.White)
                }
                IconButton(onClick = { /* Acción perfil */ }) {
                    Icon(imageVector = Icons.Default.Person, contentDescription = "Perfil", tint = Color.White)
                }
                IconButton(onClick = onSettingsClick) {
                    Icon(imageVector = Icons.Default.Settings, contentDescription = "Configuración", tint = Color.White)
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 16.dp)
                            .height(50.dp),
                        shape = RoundedCornerShape(25.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Default.Search, contentDescription = "Buscar", tint = Color.Black)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Buscar en UniRed+", color = Color.Gray, fontSize = 14.sp)
                        }
                    }
                }

                item {
                    Text(
                        text = "Historias",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 12.dp)
                    )

                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        item {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    modifier = Modifier
                                        .size(70.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFE2E6EE))
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(text = "Tu Historia", fontSize = 12.sp, color = Color.Gray)
                            }
                        }
                        items(3) {
                            Box(
                                modifier = Modifier
                                    .size(70.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFD9D9D9))
                            )
                        }
                        item {
                            Box(
                                modifier = Modifier
                                    .size(70.dp)
                                    .clip(CircleShape)
                                    .background(Color.Transparent)
                                    .clickable { onAddStoryClick() },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Agregar historia",
                                    tint = Color.White,
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF6B52D9))
                                        .padding(4.dp)
                                )
                            }
                        }
                    }
                }

                item {
                    Text(
                        text = "Recomendaciones",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(start = 16.dp, top = 32.dp, bottom = 12.dp)
                    )
                }

                items(publicacionesPrueba) { post ->
                    PostCard(post)
                }
            }
        }
    }
}

data class PostData(val nombre: String, val carrera: String, val contenido: String, val etiqueta: String)

val publicacionesPrueba = listOf(
    PostData("Elmer Correa", "Ing-Sistemas", "Alguien sabe como resolver el ejercicio de calculo vectorial", "#CalculoVectorial"),
    PostData("Yeferson Patiño", "Ing-Sistemas", "Alguien sabe como hacer una calculadora en python?", "#Programación"),
    PostData("David Esteban", "Ing-Sistemas", "Alguno para estudiar para los parciales de fisica electromagnetica?", "#FisicaElectromagnetica")
)

@Composable
fun PostCard(post: PostData) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE2E6EE))
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = post.nombre, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(text = post.carrera, color = Color.Gray, fontSize = 12.sp)
                }
                IconButton(onClick = { /* Opciones del post */ }, modifier = Modifier.size(24.dp)) {
                    Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Opciones", tint = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(text = post.contenido, fontSize = 14.sp, color = Color.Black)

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = post.etiqueta, fontSize = 14.sp, color = Color(0xFF6B52D9), fontWeight = FontWeight.Medium)
        }
    }
}
