package pe.farmacias.peruanas.cajeroexpress.prodcat.adapter.holder

import android.graphics.Paint
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import pe.farmacias.peruanas.cajeroexpress.databinding.ItemProdCatBinding
import pe.farmacias.peruanas.cajeroexpress.principal.productos.model.ProductosUi

class ProdCatHolder(val binding: ItemProdCatBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(productos: ProductosUi,clickAgregarCarrito: (ProductosUi) -> Unit,
             clickMostrarDetalles: (ProductosUi) -> Unit) {
        binding.discount.text = productos.porcentaje + " %"
        binding.textViewPrecioNormal.text = "S/." + productos.precio
        Glide.with(itemView.context).load(productos.imageL).into(binding.imageView)
        binding.textViewPrecioTachado.text = productos.preciotachado
        binding.textViewPrecioTachado.apply {
            paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
        binding.textViewNombre.text = productos.descCorta
        with(binding.root) {
            setOnClickListener {
                clickMostrarDetalles(productos)
            }
        }
        with(binding.btnAgregarCarrito) {
            setOnClickListener {
                clickAgregarCarrito(productos)
            }
        }
    }

}