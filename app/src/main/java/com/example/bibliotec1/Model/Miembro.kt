package com.example.bibliotec1.Model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "Miembros" )
data class Miembro (
    @PrimaryKey(autoGenerate = true)
    val IdMiembro: Int = 0
    ,val nombre: String
    ,val apellido: String
    ,val fechainscripcion: Long // Cambiado a Long para almacenar un timestamp
)