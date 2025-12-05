package com.ignaherner.minigastos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

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