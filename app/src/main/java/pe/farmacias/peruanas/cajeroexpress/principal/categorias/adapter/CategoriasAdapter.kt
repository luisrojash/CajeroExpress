package pe.farmacias.peruanas.cajeroexpress.principal.categorias.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import pe.farmacias.peruanas.cajeroexpress.R
import pe.farmacias.peruanas.cajeroexpress.databinding.ItemCategoriasBinding
import pe.farmacias.peruanas.cajeroexpress.principal.categorias.adapter.holder.CategoriasHolder
import pe.farmacias.peruanas.cajeroexpress.principal.categorias.model.CategoriasUi

class CategoriasAdapter(
    private val clickCategoria: (CategoriasUi) -> Unit
) : RecyclerView.Adapter<CategoriasHolder>() {

    private var categoriasList = mutableListOf<CategoriasUi>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriasHolder {
        val binding = DataBindingUtil.inflate<ItemCategoriasBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_categorias, parent, false
        )
        return CategoriasHolder(binding)
    }

    override fun getItemCount(): Int {
        return categoriasList.size
    }

    override fun onBindViewHolder(holder: CategoriasHolder, position: Int) {
        val categorias = categoriasList.get(position)
        holder.bind(categorias,clickCategoria)
    }

    fun actualizarLista(categoriasList: List<CategoriasUi>) {
        this.categoriasList.clear()
        this.categoriasList.addAll(categoriasList)
        notifyDataSetChanged()
    }
}