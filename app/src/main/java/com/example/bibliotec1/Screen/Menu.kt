package com.example.bibliotec1.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bibliotec1.R

@Composable
fun MenuScreen(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Imagen de fondo
        Image(
            painter = painterResource(id = R.drawable.librerias), // Cambia "mi_imagen" por el nombre de tu archivo
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop, // Ajustar la escala de contenido
            alpha = 1f // Ajusta la opacidad según sea necesario
        )

        // Contenido principal
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally // Centrar horizontalmente
        ) {
            // Título principal con fondo
            Box(
                modifier = Modifier
                    .padding(bottom = 70.dp)
                    .background(Color.White.copy(alpha = 0.8f), RoundedCornerShape(8.dp)) // Fondo con bordes redondeados
                    .padding(8.dp) // Espaciado interno
            ) {
                Text(
                    text = "Menú Principal",
                    style = MaterialTheme.typography.headlineMedium, // Estilo del texto
                    color = Color.Black // Color del texto
                )
            }

            // Estilo del botón
            val buttonModifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .height(60.dp) // Ajusta la altura del botón

            Button(
                onClick = { navController.navigate("autores") },
                modifier = buttonModifier,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE3F2FD)), // Color azul claro
                shape = RoundedCornerShape(10.dp) // Bordes redondeados
            ) {
                Text("Autores", color = Color.Black) // Color del texto
            }

            Button(
                onClick = { navController.navigate("libros") },
                modifier = buttonModifier,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE3F2FD)),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Libros", color = Color.Black)
            }

            Button(
                onClick = { navController.navigate("prestamos") },
                modifier = buttonModifier,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE3F2FD)),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Lista de Préstamos", color = Color.Black)
            }

            Button(
                onClick = { navController.navigate("miembros") },
                modifier = buttonModifier,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE3F2FD)),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Miembros", color = Color.Black)
            }

            Button(
                onClick = { navController.navigate("nuevo_prestamo") },
                modifier = buttonModifier,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE3F2FD)),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Préstamos", color = Color.Black)
            }
        }
    }
}
