package com.ignaherner.minigastos.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ignaherner.minigastos.data.local.Categoria

@Entity(tableName = "gastos")
data class Gasto (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, // Room lo completa al insertar
    val descripcion: String,    // EJ: "Hamburguesa"
    val monto: Double,          // EJ: 2500.00
    val categoria: Categoria,   // EJ: Categoria.COMIDA
    val fecha: Long             // Fecha en milisegundos (System.currentTimeMillis())
)