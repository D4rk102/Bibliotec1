package com.example.ejercicio_parcialii.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.bibliotec1.DAO.PrestamoDao
import com.example.bibliotec1.Model.Prestamo

//Abstract es usado para evitar crear nuevas instancias de la BD y room gestiona la relacion
@Database(entities = [Prestamo::class], version = 1, exportSchema = false)
abstract class PrestamoDatabase:RoomDatabase() {
    abstract fun prestamoDao(): PrestamoDao

    companion object {
        @Volatile
        private  var INSTANCE: PrestamoDatabase? = null

        // Permitir crear una instancia en la BD
        fun getDatabase(context: Context): PrestamoDatabase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PrestamoDatabase::class.java,
                    "prestamodatabase"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}