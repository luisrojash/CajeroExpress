package pe.farmacias.peruanas.cajeroexpress.principal.inicio.adapter.holder

import android.graphics.Paint
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import pe.farmacias.peruanas.cajeroexpress.databinding.ItemTopProductosBinding
import pe.farmacias.peruanas.cajeroexpress.principal.inicio.model.TopProducto

class TopProductoHolder(val binding: ItemTopProductosBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(producto: TopProducto, onClickHome: (TopProducto) -> Unit) {
        binding.textViewPrecioNormal.text = "S/." + producto.precio
        binding.discount.text = producto.porcentaje + " %"
        Glide.with(itemView.context).load(producto.imageL).into(binding.imageView)
        binding.textViewPrecioTachado.text = producto.preciotachado
        binding.textViewNombre.text = producto.descCorta

        binding.textViewPrecioTachado.apply {
            paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }


        with(itemView.rootView) {
            setOnClickListener {
                onClickHome(producto)
            }
        }
    }

}