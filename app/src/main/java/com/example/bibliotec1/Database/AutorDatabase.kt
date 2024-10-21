package com.example.bibliotec1.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.bibliotec1.DAO.AutorDao
import com.example.bibliotec1.Model.Autor

//Abstract es usado para evitar crear nuevas instancias de la BD y room gestiona la relacion
@Database(entities = [Autor::class], version = 2, exportSchema = false)
abstract class AutorDatabase: RoomDatabase() {
    abstract fun autorDao(): AutorDao

    companion object {
        @Volatile
        private  var INSTANCE: AutorDatabase? = null

        // Permitir crear una instancia en la BD
        fun getDatabase(context: Context): AutorDatabase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AutorDatabase::class.java,
                    "autordatabase"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}