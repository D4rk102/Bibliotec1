package com.example.bibliotec1.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bibliotec1.Model.Libro
import com.example.bibliotec1.Repository.LibroRepository
import kotlinx.coroutines.launch

@Composable
fun ScreenLibro(navController: NavController, libroRepository: LibroRepository) {
    var libros by remember { mutableStateOf(emptyList<Libro>()) }
    var titulo by remember { mutableStateOf("") }
    var genero by remember { mutableStateOf("") }
    var idAutor by remember { mutableStateOf("") } // Cambiado a String para el ID del autor
    var libroId by remember { mutableStateOf<Int?>(null) }
    var errorMessage by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

    // Cargar los libros al inicio
    LaunchedEffect(Unit) {
        libros = libroRepository.getAllLibros()
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text("Gestión de Libros", style = MaterialTheme.typography.headlineMedium)

        // Campos de entrada para título, género y ID del autor
        TextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Título") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = genero,
            onValueChange = { genero = it },
            label = { Text("Género") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = idAutor,
            onValueChange = { idAutor = it },
            label = { Text("ID del Autor") }, // Cambio para que sea ID
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Mensaje de error
        if (errorMessage.isNotEmpty()) {
            Text(errorMessage, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para agregar o actualizar libro
        Button(onClick = {
            errorMessage = ""

            // Validaciones
            if (titulo.isBlank() || genero.isBlank() || idAutor.isBlank()) {
                errorMessage = "Todos los campos son obligatorios."
                return@Button
            }

            coroutineScope.launch {
                val nuevoLibro = Libro(
                    IdLibro = libroId ?: 0,
                    titulo = titulo,
                    genero = genero,
                    IdAutor = idAutor.toInt() // Convertir ID de autor a Int
                )
                if (libroId == null) {
                    libroRepository.insertar(nuevoLibro)
                } else {
                    libroRepository.updateLibro(nuevoLibro)
                }
                // Limpiar campos después de la operación
                titulo = ""
                genero = ""
                idAutor = ""
                libroId = null
                libros = libroRepository.getAllLibros() // Actualizar la lista
            }
        }) {
            Text(if (libroId == null) "Agregar Libro" else "Actualizar Libro")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar la lista de libros
        Text("Lista de Libros", style = MaterialTheme.typography.headlineMedium)

        libros.forEach { libro ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Título: ${libro.titulo}, Autor ID: ${libro.IdAutor}")

                Row {
                    // Botón para editar libro
                    TextButton(onClick = {
                        titulo = libro.titulo
                        genero = libro.genero
                        idAutor = libro.IdAutor.toString() // Mostrar ID del autor como String
                        libroId = libro.IdLibro // Establecer el ID del libro para edición
                    }) {
                        Text("Editar")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Botón para eliminar libro
                    TextButton(onClick = {
                        coroutineScope.launch {
                            libroRepository.deleteById(libro.IdLibro)
                            libros = libroRepository.getAllLibros() // Actualizar la lista
                        }
                    }) {
                        Text("Eliminar")
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
