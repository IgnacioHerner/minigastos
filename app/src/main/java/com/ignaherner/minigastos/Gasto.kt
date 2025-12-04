package com.ignaherner.minigastos

data class Gasto (
    val descripcion: String,
    val monto: Double,
    val categoria: Categoria,
    val fecha: Long // Fecha en milisegundos
)