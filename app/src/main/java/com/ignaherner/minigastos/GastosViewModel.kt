package com.ignaherner.minigastos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class GastosViewModel(
    private val gastoDao: GastoDao
) : ViewModel() {

    private val _gastos = MutableLiveData<List<Gasto>>(emptyList())
    val gastos: LiveData<List<Gasto>> = _gastos

    init {
        // Cuando se crea el ViewModel, cargamos los datos de la DB
        cargarGastosDesdeDb()
    }

    private fun cargarGastosDesdeDb() {
        viewModelScope.launch {
            val lista = gastoDao.getAllGastos()
            _gastos.value = lista
        }
    }

    fun agregarGasto(descripcion: String, monto: Double, categoria: Categoria) {
        viewModelScope.launch {
            val nuevoGasto = Gasto (
                descripcion = descripcion,
                monto = monto,
                categoria = categoria,
                fecha = System.currentTimeMillis()
            )

            gastoDao.insertGasto(nuevoGasto)
            // Volvemos a leer de las DB para actualizar LiveData
            cargarGastosDesdeDb()
        }
    }

    fun eliminarGasto(position: Int) {
        val listaActual = _gastos.value.orEmpty()
        if (position !in listaActual.indices) return
        val gastoEliminar = listaActual[position]

        viewModelScope.launch {
            gastoDao.deleteGasto(gastoEliminar)
            cargarGastosDesdeDb()
        }
    }

    fun editarGasto(position: Int, nuevaDesc: String, nuevoMonto: Double) {
        val listaActual = _gastos.value.orEmpty()
        if (position !in listaActual.indices) return
        val viejo = listaActual[position]

        val modificado = Gasto(
            id = viejo.id,
            descripcion = nuevaDesc,
            monto = nuevoMonto,
            categoria = viejo.categoria,
            fecha = viejo.fecha
        )

        viewModelScope.launch {
            gastoDao.updateGasto(modificado)
            cargarGastosDesdeDb()
        }
    }

    fun obtenerGasto(position: Int) : Gasto? {
        val listaActual = _gastos.value.orEmpty()
        return listaActual.getOrNull(position)
    }

    fun ordenarPorFechaDesc() {
        val lista = _gastos.value.orEmpty().toMutableList()
        lista.sortByDescending { it.fecha }
        _gastos.value = lista
    }

    fun ordernarPorMontoAsc() {
        val lista = _gastos.value.orEmpty().toMutableList()
        lista.sortBy { it.monto }
        _gastos.value = lista
    }

    fun ordenarPorMontoDesc() {
        val lista = _gastos.value.orEmpty().toMutableList()
        lista.sortByDescending { it.monto }
        _gastos.value = lista
    }


}
