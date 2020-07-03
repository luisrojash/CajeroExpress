package pe.farmacias.peruanas.cajeroexpress.carrito.adapter.holder

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import pe.farmacias.peruanas.cajeroexpress.databinding.ItemCarritoBinding
import pe.farmacias.peruanas.cajeroexpress.model.Carrito

class CarritoHolder(val binding: ItemCarritoBinding) : RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SetTextI18n")
    fun bind(
        carrito: Carrito?,
        clickSumarListener: (Carrito) -> Unit,
        clickRestaListener: (Carrito) -> Unit,
        clickEliminarListener: (Carrito) -> Unit
    ) {
        binding.textViewNombreProducto.text = carrito?.nombreProducto
        binding.textViewCantidadProductos.setText(carrito?.cantidadProducto.toString())
        binding.textViewSoles.setText("S./" + carrito?.precioTotalProducto?.toDouble())
        Glide.with(itemView.context)
            .load(carrito?.imagenProducto)
            .into(binding.imageProducto)
        carrito?.let { team ->
            with(binding.imageViewSum) {
                setOnClickListener {
                    clickSumarListener(carrito)
                }
            }
            with(binding.imageViewRestar) {
                setOnClickListener {
                    clickRestaListener(carrito)
                }
            }
            with(binding.imageViewEliminar) {
                setOnClickListener {
                    clickEliminarListener(carrito)
                }
            }
        }
    }


}