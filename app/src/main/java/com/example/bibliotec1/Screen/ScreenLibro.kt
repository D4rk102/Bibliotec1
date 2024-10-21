package com.example.bibliotec1.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bibliotec1.Model.Libro
import com.example.bibliotec1.Model.Autor // Asegúrate de importar el modelo Autor
import com.example.bibliotec1.Repository.LibroRepository
import com.example.bibliotec1.Repository.AutorRepository // Asegúrate de importar el repositorio de Autor
import kotlinx.coroutines.launch

@Composable
fun ScreenLibro(navController: NavController, libroRepository: LibroRepository, autorRepository: AutorRepository) {
    var libros by remember { mutableStateOf(emptyList<Libro>()) }
    var autores by remember { mutableStateOf(emptyList<Autor>()) } // Lista de autores
    var titulo by remember { mutableStateOf("") }
    var genero by remember { mutableStateOf("") }
    var selectedAutor by remember { mutableStateOf<Autor?>(null) } // Autor seleccionado
    var libroId by remember { mutableStateOf<Int?>(null) }
    var errorMessage by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) } // Estado del diálogo

    val coroutineScope = rememberCoroutineScope()

    // Cargar los libros y autores al inicio
    LaunchedEffect(Unit) {
        libros = libroRepository.getAllLibros()
        autores = autorRepository.getAllAutores() // Cargar autores desde la base de datos
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text("Gestión de Libros", style = MaterialTheme.typography.headlineMedium)

        // Campos de entrada para título y género
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

        // Mostrar el autor seleccionado
        TextField(
            value = selectedAutor?.let { "${it.nombre} ${it.apellido}" } ?: "Seleccionar Autor",
            onValueChange = {},
            label = { Text("Autor") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDialog = true }) {
                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Seleccionar Autor")
                }
            }
        )

        // Diálogo para seleccionar un autor
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Seleccionar Autor") },
                text = {
                    Column {
                        autores.forEach { autor ->
                            TextButton(onClick = {
                                selectedAutor = autor
                                showDialog = false // Cerrar el diálogo
                            }) {
                                Text("${autor.nombre} ${autor.apellido}")
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Cerrar")
                    }
                }
            )
        }

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
            if (titulo.isBlank() || genero.isBlank() || selectedAutor == null) {
                errorMessage = "Todos los campos son obligatorios."
                return@Button
            }

            coroutineScope.launch {
                val nuevoLibro = Libro(
                    IdLibro = libroId ?: 0,
                    titulo = titulo,
                    genero = genero,
                    IdAutor = selectedAutor!!.IdAutor // Usar el ID del autor seleccionado
                )
                if (libroId == null) {
                    libroRepository.insertar(nuevoLibro)
                } else {
                    libroRepository.updateLibro(nuevoLibro)
                }
                // Limpiar campos después de la operación
                titulo = ""
                genero = ""
                selectedAutor = null
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
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp,
                    horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    val autor = autores.find { it.IdAutor == libro.IdAutor } // Obtener autor por ID
                    Text("Título: ${libro.titulo}, Autor: ${autor?.nombre} ${autor?.apellido ?: "Desconocido"}")
                }

                Row {
                    // Botón para editar libro
                    TextButton(onClick = {
                        titulo = libro.titulo
                        genero = libro.genero
                        selectedAutor = autores.find { it.IdAutor == libro.IdAutor } // Establecer autor seleccionado
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
