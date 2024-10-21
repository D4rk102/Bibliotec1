package com.example.bibliotec1.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bibliotec1.Model.Libro
import com.example.bibliotec1.Model.Autor
import com.example.bibliotec1.R
import com.example.bibliotec1.Repository.LibroRepository
import com.example.bibliotec1.Repository.AutorRepository
import kotlinx.coroutines.launch

@Composable
fun ScreenLibro(navController: NavController, libroRepository: LibroRepository, autorRepository: AutorRepository) {
    var libros by remember { mutableStateOf(emptyList<Libro>()) }
    var autores by remember { mutableStateOf(emptyList<Autor>()) }
    var titulo by remember { mutableStateOf("") }
    var genero by remember { mutableStateOf("") }
    var selectedAutor by remember { mutableStateOf<Autor?>(null) }
    var libroId by remember { mutableStateOf<Int?>(null) }
    var errorMessage by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    // Cargar los libros y autores al inicio
    LaunchedEffect(Unit) {
        libros = libroRepository.getAllLibros()
        autores = autorRepository.getAllAutores()
    }

    Image(
        painter = painterResource(id = R.drawable.librerias1),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop,
        alpha = 0.5f
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Título con fondo
            Box(
                modifier = Modifier
                    .padding(bottom = 0.dp)
                    .background(Color.White.copy(alpha = 0.8f), RoundedCornerShape(8.dp))
                    .padding(2.dp)
            ) {
                Text(
                    text = "Gestión de Libros",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.Black
                )
            }

            // Campos de entrada para título y género
            TextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = genero,
                onValueChange = { genero = it },
                label = { Text("Género") },
                modifier = Modifier.fillMaxWidth()
            )

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
                                    showDialog = false
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

            // Mensaje de error
            if (errorMessage.isNotEmpty()) {
                Text(errorMessage, color = MaterialTheme.colorScheme.error)
            }

            // Botón para agregar o actualizar libro
            Button(
                onClick = {
                    errorMessage = ""
                    if (titulo.isBlank() || genero.isBlank() || selectedAutor == null) {
                        errorMessage = "Todos los campos son obligatorios."
                        return@Button
                    }
                    coroutineScope.launch {
                        val nuevoLibro = Libro(
                            IdLibro = libroId ?: 0,
                            titulo = titulo,
                            genero = genero,
                            IdAutor = selectedAutor!!.IdAutor
                        )
                        if (libroId == null) {
                            libroRepository.insertar(nuevoLibro)
                        } else {
                            libroRepository.updateLibro(nuevoLibro)
                        }
                        titulo = ""
                        genero = ""
                        selectedAutor = null
                        libroId = null
                        libros = libroRepository.getAllLibros()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp)
                    .height(45.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE3F2FD)),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(if (libroId == null) "Agregar Libro" else "Actualizar Libro", color = Color.Black)
            }

            // Lista de libros usando LazyColumn
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 5.dp)
                    .background(Color.White.copy(alpha = 0.8f), RoundedCornerShape(8.dp))
                    .padding(8.dp)
            ) {
                Text(
                    text = "Libros",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.Black
                )
                LazyColumn {
                    items(libros) { libro ->
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
                                    val autor = autores.find { it.IdAutor == libro.IdAutor }
                                    Text("Título: ${libro.titulo}", style = MaterialTheme.typography.bodySmall)
                                    Text("Autor: ${autor?.nombre} ${autor?.apellido ?: "Desconocido"}", style = MaterialTheme.typography.bodySmall)
                                }
                                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                                    // Botón para editar libro
                                    Button(
                                        onClick = {
                                            titulo = libro.titulo
                                            genero = libro.genero
                                            selectedAutor = autores.find { it.IdAutor == libro.IdAutor }
                                            libroId = libro.IdLibro
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE3F2FD)),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Text("Editar", color = Color.Black)
                                    }
                                    Spacer(modifier = Modifier.width(5.dp))
                                    // Botón para eliminar libro
                                    Button(
                                        onClick = {
                                            coroutineScope.launch {
                                                libroRepository.deleteById(libro.IdLibro)
                                                libros = libroRepository.getAllLibros()
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
        }
        // Botón de volver al menú, fijo en la parte inferior
        Button(
            onClick = { navController.navigate("menu") },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE3F2FD)),
            shape = RoundedCornerShape(5.dp)
        ) {
            Text("Volver al Menú", color = Color.Black)
        }
    }
}
