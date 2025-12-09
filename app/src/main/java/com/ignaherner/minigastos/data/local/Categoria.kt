package com.ignaherner.minigastos.data.local

//Enum de categorias posibles para un gasto
// displayName es el texto que ve el usuario
enum class Categoria(val displayName: String) {
    COMIDA("Comida"),
    TRANSPORTE("Transporte"),
    HOGAR("Hogar"),
    SALUD("Salud"),
    OCIO("Ocio"),
    OTROS("Otros")
}