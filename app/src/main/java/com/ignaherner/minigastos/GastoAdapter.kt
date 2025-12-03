package com.ignaherner.minigastos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class GastoAdapter (
    private val gastos: MutableList<Gasto>,
    private val onItemLogClick: (Int) -> Unit,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<GastoAdapter.GastoViewHolder>() {

    inner class GastoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvDescription: TextView = itemView.findViewById(R.id.tvDescripcionItem)
        private val tvMontoItem: TextView = itemView.findViewById(R.id.tvMontoItem)

        fun bind(gasto: Gasto){
            tvDescription.text = gasto.descripcion
            tvMontoItem.text = "$${gasto.monto.format2()}"

            //Long click para eliminar
            itemView.setOnLongClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemLogClick(position)
                }
                true
            }

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