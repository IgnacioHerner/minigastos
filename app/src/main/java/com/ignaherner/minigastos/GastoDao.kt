package com.ignaherner.minigastos

import androidx.room.*


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