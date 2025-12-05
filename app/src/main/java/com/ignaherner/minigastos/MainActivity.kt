package com.ignaherner.minigastos

import android.os.Bundle
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    // ViewModel: dueño del estado
    private val viewModel: GastosViewModel by viewModels()

    // Calculadora de resúmenes
    private val gastoCalculator = GastoCalculator()

    // Adapter de la lista
    private lateinit var adapter: GastoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1) Referencias a las vistas
        val etDescripcion = findViewById<EditText>(R.id.etDescripcion)
        val etMonto = findViewById<EditText>(R.id.etMonto)
        val spCategoria = findViewById<Spinner>(R.id.spCategoria)
        val btnAgregar = findViewById<Button>(R.id.btnAgregar)
        val rvGastos = findViewById<RecyclerView>(R.id.rvGastos)
        val tvTotal = findViewById<TextView>(R.id.tvTotal)
        val tvPromedio = findViewById<TextView>(R.id.tvPromedio)
        val tvMayor = findViewById<TextView>(R.id.tvMayor)
        val tvDetalles = findViewById<TextView>(R.id.tvDetalles)
        val tvResumenCategorias = findViewById<TextView>(R.id.tvResumenCategorias)
        val btnOrdenFecha = findViewById<Button>(R.id.btnOrdenFecha)
        val btnOrdenMontoAsc = findViewById<Button>(R.id.btnOrdenMontoAsc)
        val btnOrdenMontoDesc = findViewById<Button>(R.id.btnOrdenMontoDesc)

        // 2) Configuración del Spinner de categorías
        val nombresCategorias = Categoria.values().map { it.displayName }
        val spinnerAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            nombresCategorias
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spCategoria.adapter = spinnerAdapter

        // 3) Configuración del RecyclerView + Adapter
        adapter = GastoAdapter(
            onItemLogClick = { position ->
                viewModel.eliminarGasto(position)
            },
            onItemClick = { position ->
                mostrarDialogoEditar(position)
            }
        )

        rvGastos.layoutManager = LinearLayoutManager(this)
        rvGastos.adapter = adapter

        // 4) Observar cambios en la lista de gastos del ViewModel
        viewModel.gastos.observe(this) { lista ->
            adapter.submitList(lista)
            actualizarResumen(lista, tvTotal, tvPromedio, tvMayor, tvDetalles, tvResumenCategorias)
        }

        // 5) Botón "Agregar gasto"
        btnAgregar.setOnClickListener {
            val descripcion = etDescripcion.text.toString().trim()
            val montoTexto = etMonto.text.toString().trim()

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

            viewModel.agregarGasto(descripcion, monto, categoriaSeleccionada)

            etDescripcion.text?.clear()
            etMonto.text?.clear()
        }

        // 6) Botones de orden: delegan en el ViewModel
        btnOrdenFecha.setOnClickListener {
            viewModel.ordenarPorFechaDesc()
        }

        btnOrdenMontoAsc.setOnClickListener {
            viewModel.ordernarPorMontoAsc()
        }

        btnOrdenMontoDesc.setOnClickListener {
            viewModel.ordenarPorMontoDesc()
        }
    }

    // Diálogo para editar un gasto
    private fun mostrarDialogoEditar(position: Int) {
        val gasto = viewModel.obtenerGasto(position) ?: return

        val inputDescripcion = EditText(this).apply {
            setText(gasto.descripcion)
            hint = "Descripción"
        }

        val inputMonto = EditText(this).apply {
            setText(gasto.monto.format2())
            hint = "Monto"
            inputType = android.text.InputType.TYPE_CLASS_NUMBER or
                    android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
        }

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 16, 32, 0)
            addView(inputDescripcion)
            addView(inputMonto)
        }

        AlertDialog.Builder(this)
            .setTitle("Editar gasto")
            .setView(layout)
            .setPositiveButton("Guardar") { _, _ ->
                val nuevaDesc = inputDescripcion.text.toString().trim()
                val nuevoMontoTexto = inputMonto.text.toString().trim()
                val nuevoMonto = nuevoMontoTexto.toDoubleOrNull()

                if (nuevaDesc.isEmpty() || nuevoMonto == null || nuevoMonto <= 0.0) {
                    Toast.makeText(this, "Datos inválidos", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                viewModel.editarGasto(position, nuevaDesc, nuevoMonto)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    // Calcula y muestra el resumen en base a la lista actual
    private fun actualizarResumen(
        listaGastos: List<Gasto>,
        tvTotal: TextView,
        tvPromedio: TextView,
        tvMayor: TextView,
        tvDetalles: TextView,
        tvResumenCategorias: TextView
    ) {
        if (listaGastos.isEmpty()) {
            tvTotal.text = "Total: 0.00"
            tvPromedio.text = "Promedio: 0.00"
            tvMayor.text = "Gasto más alto: -"
            tvDetalles.text = "Detalle: sin gastos"
            tvResumenCategorias.text = "Resumen por categoría: sin gastos"
            return
        }

        val montos = listaGastos.map { it.monto }

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

        tvTotal.text = "Total: ${total.format2()}"
        tvPromedio.text = "Promedio: ${promedio.format2()}"
        tvMayor.text = "Gasto más alto: ${mayor.format2()} ($nombreMayor)"

        val detalles = listaGastos.mapIndexed { index, gasto ->
            val porcentaje = porcentajes.getOrNull(index)?.format2() ?: "0.00"
            "${gasto.descripcion} (${gasto.categoria.displayName}): $porcentaje%"
        }.joinToString(" | ")

        tvDetalles.text = "Detalle: $detalles"

        val totalPorCategoria: Map<Categoria, Double> =
            listaGastos
                .groupBy { it.categoria }
                .mapValues { entry ->
                    entry.value.sumOf { it.monto }
                }

        val resumenCategoriasTexto = totalPorCategoria.entries.joinToString(" | ") { (categoria, totalCat) ->
            "${categoria.displayName}: ${totalCat.format2()}"
        }

        tvResumenCategorias.text = "Resumen por categoría: $resumenCategoriasTexto"
    }
}

// Extensiones
fun Double.format2(): String {
    return String.format(Locale.getDefault(), "%.2f", this)
}

fun Long.toDateText(): String {
    val date = Date(this)
    val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return format.format(date)
}
