package pe.farmacias.peruanas.cajeroexpress.orden.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import pe.farmacias.peruanas.cajeroexpress.dao.CarritoResp
import pe.farmacias.peruanas.cajeroexpress.databinding.OrdenDetalleBinding

class DetallesOrdenHolder(val binding: OrdenDetalleBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(ordenDetalle: CarritoResp.ObtenerOrdenDetalle) {
        binding.textViewNombreProducto.text = "Nombre : " + ordenDetalle.nombreProducto
        binding.textViewDescripcion.text = "Descripción" + ordenDetalle.descripcionCortaProducto
        binding.textViewSoles.text = "S./" + ordenDetalle.precioProducto
        Glide.with(itemView.context).load(ordenDetalle.productosImagen).into(binding.image)
    }

}