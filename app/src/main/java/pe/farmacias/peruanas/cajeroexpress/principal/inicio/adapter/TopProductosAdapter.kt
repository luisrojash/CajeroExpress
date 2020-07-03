package pe.farmacias.peruanas.cajeroexpress.principal.inicio.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import pe.farmacias.peruanas.cajeroexpress.R
import pe.farmacias.peruanas.cajeroexpress.databinding.ItemTopProductosBinding
import pe.farmacias.peruanas.cajeroexpress.principal.inicio.adapter.holder.TopProductoHolder
import pe.farmacias.peruanas.cajeroexpress.principal.inicio.model.TopProducto

class TopProductosAdapter(
    private val onClickHome: (TopProducto) -> Unit
) : RecyclerView.Adapter<TopProductoHolder>() {

    private var productoList = mutableListOf<TopProducto>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopProductoHolder {
        val binding = DataBindingUtil.inflate<ItemTopProductosBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_top_productos, parent, false
        )
        return TopProductoHolder(binding)
    }

    override fun getItemCount(): Int {
        return productoList.size
    }

    override fun onBindViewHolder(holder: TopProductoHolder, position: Int) {
        val producto = productoList.get(position)
        holder.bind(producto, onClickHome)
    }


    fun actualizarLista(listaProductos: List<TopProducto>) {
        this.productoList.clear()
        this.productoList.addAll(listaProductos)
        notifyDataSetChanged()
    }

}