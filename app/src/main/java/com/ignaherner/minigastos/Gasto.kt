package com.ignaherner.minigastos

import androidx.room.Entity
import androidx.room.PrimaryKey

// Representa UN gasto cargado por el usuario

@Entity(tableName = "gastos")
data class Gasto (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, // Room lo completa al insertar
    val descripcion: String,    // EJ: "Hamburguesa"
    val monto: Double,          // EJ: 2500.00
    val categoria: Categoria,   // EJ: Categoria.COMIDA
    val fecha: Long             // Fecha en milisegundos (System.currentTimeMillis())
)