package com.example.bibliotec1.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bibliotec1.Model.Autor
import com.example.bibliotec1.R
import com.example.bibliotec1.Repository.AutorRepository
import kotlinx.coroutines.launch

@Composable
fun ScreenAutor(navController: NavController, autorRepository: AutorRepository) {
    var autores by remember { mutableStateOf(listOf<Autor>()) }
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var nacionalidad by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Image(
        painter = painterResource(id = R.drawable.librerias1),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop,
        alpha = 0.5f
    )

    // Cargar autores al inicio
    LaunchedEffect(Unit) {
        autores = autorRepository.getAllAutores()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(2.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Título con fondo
        Box(
            modifier = Modifier
                .padding(bottom = 0.dp)
                .background(Color.White.copy(alpha = 0.8f), RoundedCornerShape(8.dp))
                .padding(2.dp)
        ) {
            Text(
                text = "Gestión de Autores",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(2.dp))

        // Campos de texto para el autor
        TextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = apellido,
            onValueChange = { apellido = it },
            label = { Text("Apellido") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = nacionalidad,
            onValueChange = { nacionalidad = it },
            label = { Text("Nacionalidad") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(5.dp))

        // Botón para agregar o actualizar un autor
        val buttonModifier = Modifier
            .fillMaxWidth()
            .padding(2.dp)
            .height(45.dp)

        Button(
            onClick = {
                if (nombre.isNotBlank() && apellido.isNotBlank() && nacionalidad.isNotBlank() &&
                    nombre.all { it.isLetter() } &&
                    apellido.all { it.isLetter() } &&
                    nacionalidad.all { it.isLetter() }
                ) {
                    coroutineScope.launch {
                        val nuevoAutor = Autor(IdAutor = 0, nombre = nombre, apellido = apellido, nacionalidad = nacionalidad)
                        autorRepository.insertar(nuevoAutor)
                        // Limpiar los campos de texto
                        nombre = ""
                        apellido = ""
                        nacionalidad = ""
                        autores = autorRepository.getAllAutores()
                        showError = false
                    }
                } else {
                    showError = true
                }
            },
            modifier = buttonModifier,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE3F2FD)),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("Agregar Autor", color = Color.Black)
        }

        // Mostrar mensaje de error si los campos están vacíos o contienen caracteres no válidos
        if (showError) {
            Text(
                text = "Por favor, complete todos los campos con solo letras.",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de autores
        Box(
            modifier = Modifier
                .weight(1f) // Permite que la lista ocupe el espacio disponible
                .padding(bottom = 5.dp)
                .background(Color.White.copy(alpha = 0.8f), RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            Text(
                text = "Autores",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.Black
            )

            LazyColumn {
                items(autores) { autor ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Nombre: ${autor.nombre}", style = MaterialTheme.typography.bodySmall)
                                Text("Apellido: ${autor.apellido}", style = MaterialTheme.typography.bodySmall)
                                Text("Nacionalidad: ${autor.nacionalidad}", style = MaterialTheme.typography.bodySmall)
                            }

                            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                                // Botón para editar autor
                                Button(
                                    onClick = {
                                        nombre = autor.nombre
                                        apellido = autor.apellido
                                        nacionalidad = autor.nacionalidad
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE3F2FD)),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text("Editar", color = Color.Black)
                                }

                                Spacer(modifier = Modifier.width(5.dp))

                                // Botón para eliminar autor
                                Button(
                                    onClick = {
                                        coroutineScope.launch {
                                            autorRepository.deleteById(autor.IdAutor)
                                            autores = autorRepository.getAllAutores()
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE3F2FD)),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text("Eliminar", color = Color.Black)
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(2.dp))

        Button(
            onClick = { navController.navigate("menu") },
            modifier = buttonModifier,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE3F2FD)),
            shape = RoundedCornerShape(5.dp)
        ) {
            Text("Volver al Menú", color = Color.Black)
        }
    }
}

