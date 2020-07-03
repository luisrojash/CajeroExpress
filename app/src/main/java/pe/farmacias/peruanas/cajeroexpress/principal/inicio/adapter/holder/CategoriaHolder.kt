package pe.farmacias.peruanas.cajeroexpress.principal.inicio.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import pe.farmacias.peruanas.cajeroexpress.databinding.ItemCategoriaBinding
import pe.farmacias.peruanas.cajeroexpress.principal.categorias.model.CategoriasUi



class CategoriaHolder(val binding: ItemCategoriaBinding) : RecyclerView.ViewHolder(binding.root) {


    fun bind(categoria: CategoriasUi, onClickCategoria: (CategoriasUi) -> Unit) {

        binding.textViewNombreCategoria.text = categoria.categoriaNombre
        binding.textViewCantidad.text = categoria.items+"+ items"
        Glide.with(itemView.context).load(categoria.imagen).into(binding.imageCategoria)

        with(itemView.rootView) {
            setOnClickListener {
                onClickCategoria(categoria)
            }
        }
    }


}


