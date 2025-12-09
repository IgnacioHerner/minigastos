package com.ignaherner.minigastos.data.model

import androidx.room.TypeConverter
import com.ignaherner.minigastos.data.local.Categoria

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