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
}