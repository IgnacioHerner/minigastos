package com.ignaherner.minigastos.data.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ignaherner.minigastos.data.model.Converters
import com.ignaherner.minigastos.data.model.GastoDao
import com.ignaherner.minigastos.data.local.Gasto

@Database(
    entities = [Gasto::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun gastoDao(): GastoDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase{
            return INSTANCE ?: synchronized(this){
                val instancia = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "minigastos_db"
                ).build()
                INSTANCE = instancia
                instancia
            }
        }
    }
}