package com.ignaherner.minigastos

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
        val spCategoria= findViewById<Spinner>(R.id.spCategoria)
        val btnAgregar = findViewById<Button>(R.id.btnAgregar)
        val rvGastos = findViewById< RecyclerView>(R.id.rvGastos)
        val tvTotal = findViewById<TextView>(R.id.tvTotal)
        val tvPromedio = findViewById<TextView>(R.id.tvPromedio)
        val tvMayor = findViewById<TextView>(R.id.tvMayor)
        val tvDetalles = findViewById<TextView>(R.id.tvDetalles)

        //Configuraciones del Spinner
        val nombresCategorias = Categoria.values().map { it.displayName }
        val spinnerAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            nombresCategorias
        )

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        spCategoria.adapter = spinnerAdapter

        adapter = GastoAdapter(
            listaGastos,
            onItemLogClick = {position ->
                eliminarGasto(position, tvTotal, tvPromedio, tvMayor, tvDetalles)
            },
            onItemClick = {position ->
                editarGasto(position, tvTotal, tvPromedio, tvMayor, tvDetalles)
            }
        )
        rvGastos.layoutManager = LinearLayoutManager(this)
        rvGastos.adapter = adapter
        actualizarResumen(tvTotal, tvPromedio, tvMayor, tvDetalles)

        // Lógica de "Agregar gasto"
        btnAgregar.setOnClickListener {
            val descripcion = etDescripcion.text.toString().trim()
            val montoTexto = etMonto.text.toString().trim()

            // Validaciones
            if (descripcion.isEmpty() || montoTexto.isEmpty()) {
                Toast.makeText(this, "Completá descripción y monto", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val monto = montoTexto.toDoubleOrNull()
            if (monto == null || monto <= 0.0) {
                Toast.makeText(this, "Ingresá un monto válido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val categoriaSeleccionada = Categoria.values()[spCategoria.selectedItemPosition]

            // Crear gasto y agregar a la lista
            val nuevoGasto = Gasto(
                descripcion = descripcion,
                monto = monto,
                categoria = categoriaSeleccionada)
            listaGastos.add(nuevoGasto)
            adapter.notifyItemInserted(listaGastos.size - 1)

            // Limpiar inputs
            etDescripcion.text?.clear()
            etMonto.text?.clear()

            // Actualizar resumen
            actualizarResumen(tvTotal, tvPromedio, tvMayor, tvDetalles)
        }
    }

    private fun actualizarResumen(
        tvTotal: TextView,
        tvPromedio: TextView,
        tvMayor: TextView,
        tvDetalles: TextView
    ) {
        if(listaGastos.isEmpty()) {
            tvTotal.text = "Total: 0.00"
            tvPromedio.text = "Promedio 0.00"
            tvMayor.text = "Gasto mas alto: -"
            tvDetalles.text = "Detalle: sin gastos"
            return
        }

        val montos = listaGastos.map {it.monto}

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
            "${gasto.descripcion} (${gasto.categoria.displayName}): $porcentaje%"
        }.joinToString(" | ")

        tvDetalles.text = "Detalle: $detalles"
    }

    private fun eliminarGasto(
        position: Int,
        tvTotal: TextView,
        tvPromedio: TextView,
        tvMayor: TextView,
        tvDetalles: TextView
    ) {
        if (position !in listaGastos.indices) return

        val gastoEliminado = listaGastos[position]
        listaGastos.removeAt(position)
        adapter.notifyItemRemoved(position)

        Toast.makeText(
            this,
            "Eliminado: ${gastoEliminado.descripcion}",
            Toast.LENGTH_SHORT
        ).show()

        actualizarResumen(tvTotal, tvPromedio, tvMayor, tvDetalles)
    }

    private fun editarGasto(
        position: Int,
        tvTotal: TextView,
        tvPromedio: TextView,
        tvMayor: TextView,
        tvDetalles: TextView
    ) {
        if (position !in listaGastos.indices) return

        val gasto = listaGastos[position]

        //Creamos inputs para el dialogo
        val inputDescripcion = EditText(this).apply {
            setText(gasto.descripcion)
            hint = "Descripcion"
        }

        val inputMonto = EditText(this).apply {
            setText(gasto.monto.format2())
            hint = "Monto"
            inputType = android.text.InputType.TYPE_CLASS_NUMBER or
                        android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
        }

        // Layaout vertical simple para meter ambos EditText
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32,16,32,0)
            addView(inputDescripcion)
            addView(inputMonto)
        }

        AlertDialog.Builder(this)
            .setTitle("Editar gasto")
            .setView(layout)
            .setPositiveButton ("Guardar"){ _, _ ->
                val nuevaDesc = inputDescripcion.text.toString().trim()
                val nuevoMontoTexto = inputMonto.text.toString().trim()
                val nuevoMonto = nuevoMontoTexto.toDoubleOrNull()

                if(nuevaDesc.isEmpty() || nuevoMonto == null || nuevoMonto <=0.0) {
                    Toast.makeText(this, "Datos invalidos", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                //Actualizamos el gasto
                listaGastos[position] = Gasto(
                    nuevaDesc,
                    nuevoMonto,
                    categoria = gasto.categoria)
                adapter.notifyItemChanged(position)

                actualizarResumen(tvTotal, tvPromedio, tvMayor, tvDetalles)
            }
            .setNegativeButton ("Cancelar", null)
            .show()
    }

}


// Función de extensión para formatear con 2 decimales
fun Double.format2(): String {
    return String.format(Locale.getDefault(), "%.2f", this)
}