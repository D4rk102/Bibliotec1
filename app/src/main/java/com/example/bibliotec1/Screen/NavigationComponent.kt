package com.example.bibliotec1.Screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bibliotec1.Database.AutorDatabase
import com.example.bibliotec1.Database.LibroDatabase
import com.example.bibliotec1.Database.MiembroDatabase // Importa la base de datos de miembros
import com.example.bibliotec1.Repository.AutorRepository
import com.example.bibliotec1.Repository.LibroRepository
import com.example.bibliotec1.Repository.MiembroRepository // Importa el repositorio de miembros
import com.example.bibliotec1.Screen.ScreenAutor
import com.example.bibliotec1.Screen.ScreenLibro
import com.example.bibliotec1.Screen.ScreenListaPrestamo
import com.example.bibliotec1.Screen.ScreenMiembro
import com.example.bibliotec1.Screen.ScreenPrestamo

@Composable
fun NavigationComponent() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val libroRepository = LibroRepository(LibroDatabase.getDatabase(context).libroDao())

    // Crear instancia del AutorRepository
    val autorRepository = AutorRepository(AutorDatabase.getDatabase(context).autorDao())

    // Crear instancia del MiembroRepository
    val miembroRepository = MiembroRepository(MiembroDatabase.getDatabase(context).miembroDao())

    NavHost(navController = navController, startDestination = "menu") {
        composable("menu") {
            MenuScreen(navController)
        }
        composable("autores") {
            ScreenAutor(navController, autorRepository)
        }
        composable("libros") {
            ScreenLibro(navController, libroRepository)
        }
        composable("prestamos") {
            ScreenListaPrestamo(navController)
        }
        composable("miembros") {
            ScreenMiembro(navController, miembroRepository) // Pasar el miembroRepository
        }
        composable("nuevo_prestamo") {
            ScreenPrestamo(navController)
        }
    }
}