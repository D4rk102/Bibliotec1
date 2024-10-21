package com.example.bibliotec1.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.bibliotec1.Model.Libro

@Dao
interface LibrosDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(libro: Libro)

    @Query("SELECT * FROM Libros")
    suspend fun getAllLibros(): List<Libro>

    @Query("DELETE FROM Libros WHERE IdLibro = :Idlibro")
    suspend fun deleteById(Idlibro: Int): Int

    @Update
    suspend fun updateLibro(libro: Libro)
}
