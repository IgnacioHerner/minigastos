package com.ignaherner.minigastos

class GastoCalculator {


    fun calcularTotal (gastos: List<Double>): Double {
        return gastos.sum()
    }

    fun calcularPromedio(gastos: List<Double>): Double {
        if(gastos.isEmpty()) return 0.0
        val total = gastos.sum()
        return total / gastos.size
    }

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

    // Porcentajes de cada gasto sobre el total
    fun calcularPorcentajes(gastos: List<Double>): List<Double> {
        val total = calcularTotal(gastos)
        if (total == 0.0) {
            // Si esl total es 0, que todos los porcentajes sean 0
            return gastos.map { 0.0 }
        }
        return gastos.map { (it / total) * 100 }
    }

    // Indice del gasto mas alto ( 0, 1 o 2)
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