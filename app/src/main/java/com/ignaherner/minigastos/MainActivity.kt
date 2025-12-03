package com.ignaherner.minigastos

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private val gastoCalculator = GastoCalculator()
    private val listaGastos = mutableListOf<Gasto>()
    private lateinit var adapter: GastoAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1) Referencias a las vistas
        val etDescripcion = findViewById<EditText>(R.id.etDescripcion)
        val etMonto = findViewById<EditText>(R.id.etMonto)
        val btnAgregar = findViewById<Button>(R.id.btnAgregar)
        val rvGastos = findViewById< RecyclerView>(R.id.rvGastos)
        val tvTotal = findViewById<TextView>(R.id.tvTotal)
        val tvPromedio = findViewById<TextView>(R.id.tvPromedio)
        val tvMayor = findViewById<TextView>(R.id.tvMayor)
        val tvDetalles = findViewById<TextView>(R.id.tvDetalles)

        adapter = GastoAdapter(listaGastos)
        rvGastos.layoutManager = LinearLayoutManager(this)
        rvGastos.adapter = adapter

        // 2) Logica al apretar el botón
        btnAgregar.setOnClickListener {
            val descripcion = etDescripcion.text.toString().trim()
            val montoTexto = etMonto.text.toString().trim()

            if(descripcion.isEmpty() || montoTexto.isEmpty()) {
                Toast.makeText(this, "Completa descripcion y monto", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val monto = montoTexto.toDoubleOrNull()
            if(monto == null || monto <= 0.0) {
                Toast.makeText(this, "Ingresa un monto valido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Agregar gasto a la lista
            val nuevoGasto = Gasto(descripcion = descripcion, monto = monto)
            listaGastos.add(nuevoGasto)
            adapter.notifyItemInserted(listaGastos.size - 1)

            // Limpiar inputs
            etDescripcion.text?.clear()
            etMonto.text?.clear()


            // Recalcular resumen
            val montos = listaGastos.map { it.monto }


            // Usar funciones de Kotlin para calcular
            val total = gastoCalculator.calcularTotal(montos)
            val promedio = gastoCalculator.calcularPromedio(montos)
            val mayor = gastoCalculator.gastoMasAlto(montos)
            val porcentajes = gastoCalculator.calcularPorcentajes(montos)
            val indiceMayor = gastoCalculator.obtenerIndiceGastoMasAlto(montos)

            val nombreMayor = if (indiceMayor in listaGastos.indices) {
                listaGastos[indiceMayor].descripcion
            } else {
                "-"
            }

            // Mostrar resultados
            tvTotal.text = "Total: ${total.format2()}"
            tvPromedio.text = "Promedio: ${promedio.format2()}"
            tvMayor.text = "Gasto más alto: ${mayor.format2()} ($nombreMayor)"

            val detalles = listaGastos.mapIndexed { index, gasto ->
                val porcentaje = porcentajes.getOrNull(index)?.format2() ?: "0.00"
                "${gasto.descripcion} : $porcentaje%"
            }.joinToString(" | ")

            tvDetalles.text = "Detalle: $detalles"
        }
    }
}
// Función de extensión para formatear con 2 decimales
fun Double.format2(): String {
    return String.format(Locale.getDefault(), "%.2f", this)
}