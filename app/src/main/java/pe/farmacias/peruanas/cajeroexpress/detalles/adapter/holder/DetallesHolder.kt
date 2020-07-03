package pe.farmacias.peruanas.cajeroexpress.detalles.adapter.holder

import com.bumptech.glide.Glide
import com.smarteist.autoimageslider.SliderViewAdapter
import pe.farmacias.peruanas.cajeroexpress.databinding.ItemDetallesBinding
import pe.farmacias.peruanas.cajeroexpress.detalles.ui.DetallesUi

class DetallesHolder(val binding: ItemDetallesBinding) :
    SliderViewAdapter.ViewHolder(binding.root) {

    fun bind(detalles: DetallesUi) {
        Glide.with(binding.root)
            .load(detalles.imageView)
            .fitCenter()
            .into(binding.ivAutoImageSlider)
        binding.txtoffer.text = detalles.porcentaje+" %"
    }
}