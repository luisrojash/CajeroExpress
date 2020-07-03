package pe.farmacias.peruanas.cajeroexpress.orden.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import pe.farmacias.peruanas.cajeroexpress.R
import pe.farmacias.peruanas.cajeroexpress.databinding.ItemOrdenBinding
import pe.farmacias.peruanas.cajeroexpress.model.Orden
import pe.farmacias.peruanas.cajeroexpress.orden.adapter.holder.OrdenHolder
import pe.farmacias.peruanas.cajeroexpress.orden.model.OrdenUi


class OrdenAdapter(
    private val onLongClickAnular: (OrdenUi) -> Unit
) : RecyclerView.Adapter<OrdenHolder>() {

    private var ordenList = mutableListOf<OrdenUi>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdenHolder {
        val binding = DataBindingUtil.inflate<ItemOrdenBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_orden, parent, false
        )
        return OrdenHolder(binding)
    }

    override fun getItemCount(): Int {
        return ordenList.size
    }

    override fun onBindViewHolder(holder: OrdenHolder, position: Int) {
        val orden = ordenList.get(position)
        holder.bind(orden,onLongClickAnular)
    }

    fun actualizarLista(categoriasList: List<OrdenUi>) {
        this.ordenList.clear()
        this.ordenList.addAll(categoriasList)
        notifyDataSetChanged()
    }
}