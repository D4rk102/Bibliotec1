package com.example.bibliotec1.Model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date


@Entity(tableName = "Prestamos" )
data class Prestamo (
    @PrimaryKey(autoGenerate = true)
    val IdPrestamo: Int = 0
    ,val IdLibro: Int
    ,val IdMiembro: Int
    ,val fecha_prestamo: Long // Cambiado a Long para almacenar un timestamp
    ,val fecha_devolucion: Long // Cambiado a Long para almacenar un timestamp
)