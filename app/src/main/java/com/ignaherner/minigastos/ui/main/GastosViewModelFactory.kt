package com.ignaherner.minigastos.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ignaherner.minigastos.data.model.GastoDao

class GastosViewModelFactory(
    private val gastoDao: GastoDao
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GastosViewModel::class.java)) {
            return GastosViewModel(gastoDao) as T
        }
        throw IllegalArgumentException("Unknow ViewModel class")
    }
}