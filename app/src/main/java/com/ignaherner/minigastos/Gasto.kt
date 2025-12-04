package com.ignaherner.minigastos

data class Gasto (
    val descripcion: String,
    val monto: Double,
    val categoria: Categoria
)