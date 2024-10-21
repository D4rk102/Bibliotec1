package com.example.bibliotec1.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bibliotec1.Model.Autor
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

    // Cargar autores al inicio
    LaunchedEffect(Unit) {
        autores = autorRepository.getAllAutores()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text("Gestión de Autores", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

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

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para agregar o actualizar un autor
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
                        showError = false // Ocultar el mensaje de error si se agrega correctamente
                    }
                } else {
                    showError = true // Mostrar el mensaje de error si los campos están vacíos o no son letras
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Agregar Autor")
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
        Text("Lista de Autores:", style = MaterialTheme.typography.headlineSmall)

        LazyColumn {
            items(autores) { autor ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
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
                            Text("Nombre: ${autor.nombre}", style = MaterialTheme.typography.bodyLarge)
                            Text("Apellido: ${autor.apellido}", style = MaterialTheme.typography.bodyMedium)
                            Text("Nacionalidad: ${autor.nacionalidad}", style = MaterialTheme.typography.bodyMedium)
                        }

                        Row(horizontalArrangement = Arrangement.SpaceBetween) {
                            // Botón para editar autor
                            Button(onClick = {
                                nombre = autor.nombre
                                apellido = autor.apellido
                                nacionalidad = autor.nacionalidad
                            }) {
                                Text("Editar")
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            // Botón para eliminar autor
                            Button(onClick = {
                                coroutineScope.launch {
                                    autorRepository.deleteById(autor.IdAutor)
                                    autores = autorRepository.getAllAutores()
                                }
                            }) {
                                Text("Eliminar")
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = { navController.navigate("menu") }) {
            Text("Volver al Menú")
        }
    }
}

