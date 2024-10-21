package com.example.bibliotec1.Repository

import com.example.bibliotec1.DAO.MiembroDao
import com.example.bibliotec1.Model.Miembro

class MiembroRepository(private val miembroDao: MiembroDao) {
    suspend fun insertar(miembro: Miembro){
        miembroDao.insert(miembro)
    }
    suspend fun getAllMiembros(): List<Miembro>{
        return miembroDao.getAllMiembros()
    }
    suspend fun deleteById(Idmiembro:Int):Int {
        return miembroDao.deleteById(Idmiembro)
    }
    suspend fun updateMiembro(miembro: Miembro) {
        miembroDao.updateMiembro(miembro)
    }
}