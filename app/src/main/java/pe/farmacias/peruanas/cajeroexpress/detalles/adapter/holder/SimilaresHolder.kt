package pe.farmacias.peruanas.cajeroexpress.detalles.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import pe.farmacias.peruanas.cajeroexpress.databinding.ItemSimilaresBinding
import pe.farmacias.peruanas.cajeroexpress.detalles.ui.SimilaresUi

class SimilaresHolder(val binding: ItemSimilaresBinding) : RecyclerView.ViewHolder(binding.root) {


    fun bind(similares: SimilaresUi) {
        binding.discount.text = similares.pocentaje +" %"
        Glide.with(itemView.context).load(similares.imageM).into(binding.imageView)
        binding.textViewPrecioNormal.text = "S/."+ similares.precio
        binding.textViewPrecioTachado.text = similares.preciotachado
        binding.textViewNombre.text = similares.nombre
    }

}