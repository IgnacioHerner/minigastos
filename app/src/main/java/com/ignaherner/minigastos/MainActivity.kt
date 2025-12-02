package com.ignaherner.minigastos

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1) Referencias a las vistas
        val etGasto1 = findViewById<EditText>(R.id.etGasto1)
        val etGasto2 = findViewById<EditText>(R.id.etGasto2)
        val etGasto3 = findViewById<EditText>(R.id.etGasto3)
        val btnCalcular = findViewById<Button>(R.id.btnCalcular)
        val tvTotal = findViewById<TextView>(R.id.tvTotal)
        val tvPromedio = findViewById<TextView>(R.id.tvPromedio)
        val tvMayor = findViewById<TextView>(R.id.tvMayor)

        // 2) Logica al apretar el botón
        btnCalcular.setOnClickListener {
            //Leer texto de los EditText y convertir a Double (vacio = 0.0)
            val gasto1 = etGasto1.text.toString().toDoubleOrNull() ?: 0.0
            val gasto2 = etGasto2.text.toString().toDoubleOrNull() ?: 0.0
            val gasto3 = etGasto3.text.toString().toDoubleOrNull() ?: 0.0

            // Armar lista
            val listaGastos = listOf(gasto1, gasto2, gasto3)

            // Usar funciones de Kotlin para calcular
            val total = calcularTotal(listaGastos)
            val promedio = calcularPromedio(listaGastos)
            val mayor = gastoMasAlto(listaGastos)

            // Mostrar resultados
            tvTotal.text = "Total: $total"
            tvPromedio.text = "Promedio: $promedio"
            tvMayor.text = "Gasto más alto: $mayor"
        }
    }

    // -------- Funciones de lógica (puras, sin UI) --------

    private fun calcularTotal (gastos: List<Double>): Double {
        return gastos.sum()
    }

    private fun calcularPromedio(gastos: List<Double>): Double {
        if(gastos.isEmpty()) return 0.0
        val total = gastos.sum()
        return total / gastos.size
    }

    private fun gastoMasAlto(gastos: List<Double>): Double{
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