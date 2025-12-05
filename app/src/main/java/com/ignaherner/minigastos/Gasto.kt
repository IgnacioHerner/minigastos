package com.ignaherner.minigastos

// Representa UN gasto cargado por el usuario
data class Gasto (
    val descripcion: String,    // EJ: "Hamburguesa"
    val monto: Double,          // EJ: 2500.00
    val categoria: Categoria,   // EJ: Categoria.COMIDA
    val fecha: Long             // Fecha en milisegundos (System.currentTimeMillis())
)