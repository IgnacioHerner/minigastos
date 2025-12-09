package com.ignaherner.minigastos.data.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ignaherner.minigastos.data.local.Gasto

@Dao
interface GastoDao {

    @Query("SELECT * FROM gastos ORDER BY fecha DESC")
    suspend fun getAllGastos(): List<Gasto>

    @Insert
    suspend fun insertGasto(gasto: Gasto)

    @Update
    suspend fun updateGasto(gasto: Gasto)

    @Delete
    suspend fun deleteGasto(gasto: Gasto)
}