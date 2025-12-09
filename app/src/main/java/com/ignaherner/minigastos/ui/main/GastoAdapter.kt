package com.ignaherner.minigastos.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ignaherner.minigastos.R
import com.ignaherner.minigastos.data.local.Gasto
import com.ignaherner.minigastos.domain.format2
import com.ignaherner.minigastos.domain.toDateText

// Adapter que sabe cómo dibujar la lista de Gasto en el RecyclerView.
class GastoAdapter (
    // Callback para long click (borrar)
    private val onItemLogClick: (Int) -> Unit,
    // Callback para click normal (editar)
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<GastoAdapter.GastoViewHolder>() {

    // Lista interna que el adapter muestra
    private val gastos = mutableListOf<Gasto>()

    // Metodo para actualizar la lista desde afuera (Activity)
    fun submitList(nuevaLista: List<Gasto>) {
        gastos.clear()
        gastos.addAll(nuevaLista)
        notifyDataSetChanged()
    }

    // ViewHolder: representa UNA fila de la lista.
    inner class GastoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvDescription: TextView = itemView.findViewById(R.id.tvDescripcionItem)
        private val tvMontoItem: TextView = itemView.findViewById(R.id.tvMontoItem)
        private val tvFechaItem: TextView = itemView.findViewById(R.id.tvFechaItem)

        // Enlaza un Gasto con las views del item
        fun bind(gasto: Gasto){
            tvDescription.text = "${gasto.descripcion} (${gasto.categoria.displayName})"
            tvMontoItem.text = "$${gasto.monto.format2()}"
            tvFechaItem.text = gasto.fecha.toDateText() // Mostrar fecha

            // Long click -> borrar
            itemView.setOnLongClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemLogClick(position)
                }
                true
            }

            // Click normal -> editar
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(position)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GastoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_gasto, parent, false)
        return GastoViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: GastoViewHolder,
        position: Int
    ) {
        holder.bind(gastos[position])
    }

    override fun getItemCount(): Int = gastos.size

}