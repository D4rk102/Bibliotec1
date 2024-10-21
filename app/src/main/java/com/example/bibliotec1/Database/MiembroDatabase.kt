package com.example.bibliotec1.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.bibliotec1.DAO.MiembroDao
import com.example.bibliotec1.Model.Miembro

//Abstract es usado para evitar crear nuevas instancias de la BD y room gestiona la relacion
@Database(entities = [Miembro::class], version = 2, exportSchema = false)
abstract class MiembroDatabase: RoomDatabase() {
    abstract fun miembroDao(): MiembroDao

    companion object{
        @Volatile
        private  var INSTANCE: MiembroDatabase? = null

        // Permitir crear una instancia en la BD
        fun getDatabase(context: Context): MiembroDatabase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MiembroDatabase::class.java,
                    "miembrodatabase"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}