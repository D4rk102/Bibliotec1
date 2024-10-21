package com.example.bibliotec1.Repository

import com.example.bibliotec1.DAO.AutorDao
import com.example.bibliotec1.Model.Autor

class AutorRepository(private val autorDao: AutorDao) {
    suspend fun insertar(autor: Autor){
        autorDao.insert(autor)
    }
    suspend fun getAllAutores(): List<Autor>{
        return autorDao.getAllAutores()
    }
    suspend fun deleteById(Idautor:Int):Int {
        return autorDao.deleteById(Idautor)
    }
    suspend fun updateAutor(autor: Autor) {
        autorDao.updateAutor(autor)
    }

}