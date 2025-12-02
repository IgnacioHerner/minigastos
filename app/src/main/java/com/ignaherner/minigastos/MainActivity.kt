package com.ignaherner.minigastos

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private val gastoCalculator = GastoCalculator()


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
        val tvDetalles = findViewById<TextView>(R.id.tvDetalles)

        // 2) Logica al apretar el botón
        btnCalcular.setOnClickListener {
            //Leer texto de los EditText y convertir a Double (vacio = 0.0)
            val gasto1 = etGasto1.text.toString().toDoubleOrNull() ?: 0.0
            val gasto2 = etGasto2.text.toString().toDoubleOrNull() ?: 0.0
            val gasto3 = etGasto3.text.toString().toDoubleOrNull() ?: 0.0

            // Armar lista
            val listaGastos = listOf(gasto1, gasto2, gasto3)

            // Validacion: todos son 0 -> no tiene sentido calcular
            if(listaGastos.all { it == 0.0}) {
                Toast.makeText(this, "Ingresa al menos un gasto", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Usar funciones de Kotlin para calcular
            val total = gastoCalculator.calcularTotal(listaGastos)
            val promedio = gastoCalculator.calcularPromedio(listaGastos)
            val mayor = gastoCalculator.gastoMasAlto(listaGastos)
            val porcentajes = gastoCalculator.calcularPorcentajes(listaGastos)
            val indiceMayor = gastoCalculator.obtenerIndiceGastoMasAlto(listaGastos)

            val nombreMayor = when (indiceMayor) {
                0 -> "Gasto 1"
                1 -> "Gasto 2"
                2 -> "Gasto 3"
                else -> "-"
            }

            // Mostrar resultados
            tvTotal.text = "Total: ${total.format2()}"
            tvPromedio.text = "Promedio: ${promedio.format2()}"
            tvMayor.text = "Gasto más alto: ${mayor.format2()} ($nombreMayor)"

            val p1 = porcentajes.getOrNull(0)?.format2() ?: "0.00"
            val p2 = porcentajes.getOrNull(1)?.format2() ?: "0.00"
            val p3 = porcentajes.getOrNull(2)?.format2() ?: "0.00"

            tvDetalles.text = "Detalle: G1: $p1% | G2: $p2% | G3: $p3%"
        }
    }
}
// Función de extensión para formatear con 2 decimales
private fun Double.format2(): String {
    return String.format(Locale.getDefault(), "%.2f", this)
}