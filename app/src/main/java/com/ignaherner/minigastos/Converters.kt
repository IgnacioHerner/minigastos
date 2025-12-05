package com.ignaherner.minigastos

import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun fromCategoria(categoria: Categoria): String {
        return categoria.name // Comida, Transporte etc
    }

    @TypeConverter
    fun toCategoria(nombre: String): Categoria {
        return Categoria.valueOf(nombre)
    }

}