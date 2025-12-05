package com.ignaherner.minigastos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GastosViewModel : ViewModel() {

    // LiveData que expone la lista de gastos a la UI
    private val _gastos = MutableLiveData<List<Gasto>>(emptyList())
    val gastos: LiveData<List<Gasto>> = _gastos

    // Devuelve siempre una lista mutable para operar
    private fun listaMutable(): MutableList<Gasto> {
        return _gastos.value.orEmpty().toMutableList()
    }

    fun agregarGasto(descripcion: String, monto: Double, categoria: Categoria) {
        val lista = listaMutable()
        val nuevoGasto = Gasto(
            descripcion = descripcion,
            monto = monto,
            categoria = categoria,
            fecha = System.currentTimeMillis()
        )
        lista.add(nuevoGasto)
        _gastos.value = lista
    }

    fun eliminarGasto(position: Int) {
        val lista = listaMutable()
        if(position !in lista.indices) return
        lista.removeAt(position)
        _gastos.value = lista
    }

    fun editarGasto(position: Int, nuevaDesc: String, nuevoMonto: Double) {
        val lista =listaMutable()
        if(position !in lista.indices) return

        val gastoViejo = lista[position]
        val gastoNuevo = Gasto(
            descripcion = nuevaDesc,
            monto = nuevoMonto,
            categoria = gastoViejo.categoria,
            fecha = gastoViejo.fecha
        )
        lista[position] = gastoNuevo
        _gastos.value = lista
    }

    fun obtenerGasto(position: Int): Gasto? {
        return _gastos.value?.getOrNull(position)
    }

    fun ordenarPorFechaDesc() {
        val lista = listaMutable()
        lista.sortByDescending { it.fecha }
        _gastos.value = lista
    }

    fun ordernarPorMontoAsc() {
        val lista = listaMutable()
        lista.sortBy { it.monto }
        _gastos.value = lista
    }

    fun ordenarPorMontoDesc() {
        val lista = listaMutable()
        lista.sortByDescending { it.monto }
        _gastos.value = lista
    }
}