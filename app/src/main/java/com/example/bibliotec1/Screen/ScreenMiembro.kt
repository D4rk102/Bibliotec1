package com.example.bibliotec1.Screen

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bibliotec1.Model.Miembro
import com.example.bibliotec1.Repository.MiembroRepository
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

@Composable
fun ScreenMiembro(navController: NavController, miembroRepository: MiembroRepository) {
    var miembros = remember { mutableStateListOf<Miembro>() }
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var fechaIngreso by remember { mutableStateOf(LocalDate.now()) }
    var miembroId by remember { mutableStateOf<Int?>(null) }
    var errorMessage by remember { mutableStateOf("") } // Mensaje de error

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    // Cargar los miembros al inicio
    LaunchedEffect(Unit) {
        val listaMiembros = miembroRepository.getAllMiembros()
        miembros.clear()
        miembros.addAll(listaMiembros)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text("Gestión de Miembros", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de texto para el nombre
        TextField(
            value = nombre,
            onValueChange = {
                nombre = it
                errorMessage = "" // Limpiar mensaje de error al cambiar el nombre
            },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo de texto para el apellido
        TextField(
            value = apellido,
            onValueChange = {
                apellido = it
                errorMessage = "" // Limpiar mensaje de error al cambiar el apellido
            },
            label = { Text("Apellido") },
            modifier = Modifier.fillMaxWidth()
        )

        // Mostrar mensaje de error si existe
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Botón para seleccionar fecha de ingreso
        Button(onClick = {
            val date = Calendar.getInstance()
            date.set(fechaIngreso.year, fechaIngreso.monthValue - 1, fechaIngreso.dayOfMonth)

            DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    fechaIngreso = LocalDate.of(year, month + 1, dayOfMonth)
                },
                date.get(Calendar.YEAR),
                date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH)
            ).apply {
                date.set(2024, 9, 10)
                datePicker.minDate = date.timeInMillis
            }.show()
        }) {
            Text("Seleccionar Fecha de Ingreso")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Fecha seleccionada: $fechaIngreso")

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para agregar o actualizar miembro
        Button(
            onClick = {
                // Validaciones
                if (nombre.isBlank() || apellido.isBlank()) {
                    errorMessage = "Los campos nombre y apellido no pueden estar vacíos."
                    return@Button
                }
                if (!nombre.all { it.isLetter() } || !apellido.all { it.isLetter() }) {
                    errorMessage = "Los campos nombre y apellido solo pueden contener letras."
                    return@Button
                }

                coroutineScope.launch {
                    val timestamp = fechaIngreso.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                    val nuevoMiembro = Miembro(
                        IdMiembro = miembroId ?: 0,
                        nombre = nombre,
                        apellido = apellido,
                        fechainscripcion = timestamp
                    )
                    if (miembroId == null) {
                        miembroRepository.insertar(nuevoMiembro)
                    } else {
                        miembroRepository.updateMiembro(nuevoMiembro)
                    }
                    // Limpiar los campos después de la operación
                    nombre = ""
                    apellido = ""
                    fechaIngreso = LocalDate.now()
                    miembroId = null

                    // Actualizar la lista de miembros
                    val actualizados = miembroRepository.getAllMiembros()
                    miembros.clear()
                    miembros.addAll(actualizados)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (miembroId == null) "Agregar Miembro" else "Actualizar Miembro")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de miembros
        Text("Lista de Miembros", style = MaterialTheme.typography.headlineSmall)

        miembros.forEach { miembro ->
            Card(
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    val fechaIngresoLocalDate =
                        Instant.ofEpochMilli(miembro.fechainscripcion).atZone(ZoneId.systemDefault()).toLocalDate()
                    Text("Nombre: ${miembro.nombre} ${miembro.apellido}", style = MaterialTheme.typography.bodyLarge)
                    Text("Fecha de Ingreso: $fechaIngresoLocalDate", style = MaterialTheme.typography.bodyMedium)

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        // Botón para editar miembro
                        TextButton(onClick = {
                            nombre = miembro.nombre
                            apellido = miembro.apellido
                            fechaIngreso =
                                Instant.ofEpochMilli(miembro.fechainscripcion).atZone(ZoneId.systemDefault()).toLocalDate()
                            miembroId = miembro.IdMiembro // Establecer el ID del miembro para edición
                        }) {
                            Text("Editar")
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Botón para eliminar miembro
                        TextButton(onClick = {
                            coroutineScope.launch {
                                miembroRepository.deleteById(miembro.IdMiembro)
                                val actualizados = miembroRepository.getAllMiembros()
                                miembros.clear()
                                miembros.addAll(actualizados)
                            }
                        }) {
                            Text("Eliminar")
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
