package com.ignaherner.minigastos

// Clase responsable de toda la logica de calculo
// NO sabe nada de Android, solo numeros y listas
class GastoCalculator {

    // Suma todos los montos
    fun calcularTotal (gastos: List<Double>): Double {
        return gastos.sum()
    }

    // Calcula el promedio de los montos.
    fun calcularPromedio(gastos: List<Double>): Double {
        if(gastos.isEmpty()) return 0.0
        val total = gastos.sum()
        return total / gastos.size
    }

    // Devuelve el monto más alto de la lista.
    fun gastoMasAlto(gastos: List<Double>): Double{
        if (gastos.isEmpty()) return 0.0

        var mayor = gastos[0]
        for (gasto in gastos) {
            if (gasto > mayor) {
                mayor = gasto
            }
        }
        return mayor
    }

    // Devuelve una lista con el porcentaje de cada gasto respecto al total.
    // Ej: [100.0, 50.0, 25.0] (no necesariamente suman EXACTO 100 por decimales).
    fun calcularPorcentajes(gastos: List<Double>): List<Double> {
        val total = calcularTotal(gastos)
        if (total == 0.0) {
            // Si esl total es 0, que todos los porcentajes sean 0
            return gastos.map { 0.0 }
        }
        return gastos.map { (it / total) * 100 }
    }

    // Devuelve la posición del gasto más alto en la lista (0, 1, 2, ...).
    // Si la lista está vacía, -1.
    fun obtenerIndiceGastoMasAlto(gastos: List<Double>) : Int {
        if (gastos.isEmpty()) return -1

        var mayor = gastos[0]
        var indiceMayor = 0

        for((index, gasto) in gastos.withIndex()){
            if(gasto > mayor) {
                mayor = gasto
                indiceMayor = index
            }
        }
        return indiceMayor
    }
}