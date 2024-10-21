package com.example.bibliotec1.Repository

import com.example.bibliotec1.DAO.LibrosDao
import com.example.bibliotec1.Model.Libro

class LibroRepository(private val librosDao: LibrosDao) {
    suspend fun insertar(libro: Libro) {
        librosDao.insert(libro)
    }

    suspend fun getAllLibros(): List<Libro> {
        return librosDao.getAllLibros()
    }

    suspend fun deleteById(Idlibro: Int): Int {
        return librosDao.deleteById(Idlibro)
    }

    suspend fun updateLibro(libro: Libro) {
        librosDao.updateLibro(libro)
    }
}
