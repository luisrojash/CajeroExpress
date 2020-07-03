package pe.farmacias.peruanas.cajeroexpress.principal.categorias.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import pe.farmacias.peruanas.cajeroexpress.databinding.ItemCategoriasBinding
import pe.farmacias.peruanas.cajeroexpress.principal.categorias.model.CategoriasUi

class CategoriasHolder(val binding: ItemCategoriasBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(categorias: CategoriasUi,  clickCategoria: (CategoriasUi) -> Unit) {

        binding.textViewNombreCategoria.text = categorias.categoriaNombre
        Glide.with(itemView.context).load(categorias.imagen).into(binding.imageViewCategoria)

        with(binding.root) {
            setOnClickListener {
                clickCategoria(categorias)
            }
        }
    }

}