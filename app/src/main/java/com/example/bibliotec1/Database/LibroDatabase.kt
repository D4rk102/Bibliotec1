package com.example.bibliotec1.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.bibliotec1.DAO.LibrosDao
import com.example.bibliotec1.Model.Libro

@Database(entities = [Libro::class], version = 2, exportSchema = false)
abstract class LibroDatabase : RoomDatabase() {
    abstract fun libroDao(): LibrosDao

    companion object {
        @Volatile
        private var INSTANCE: LibroDatabase? = null

        fun getDatabase(context: Context): LibroDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LibroDatabase::class.java,
                    "librodatabase"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
