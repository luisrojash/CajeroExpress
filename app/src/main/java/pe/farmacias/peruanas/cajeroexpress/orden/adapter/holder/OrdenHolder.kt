package pe.farmacias.peruanas.cajeroexpress.orden.adapter.holder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pe.farmacias.peruanas.cajeroexpress.databinding.ItemOrdenBinding
import pe.farmacias.peruanas.cajeroexpress.orden.adapter.DetallesOrdenAdapter
import pe.farmacias.peruanas.cajeroexpress.orden.model.OrdenUi
import timber.log.Timber

class OrdenHolder(val binding: ItemOrdenBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(orden: OrdenUi, clickOnLongAnular: (OrdenUi) -> Unit) {
        binding.number.text = orden.conteo
        binding.textViewFecha.text = "Fecha : " + orden.fecha
        binding.textViewSoles.text = "S./" + orden.totalSoles
        binding.textViewNombreUsuario.text = "Usuario: " + orden.nombreUsuario
        binding.textViewCantidadProductos.text = "Cantidad Pr.Vendidos: " + orden.totalCantidad
        binding.textViewEstado.text = orden.estado
        val ordenDetallesAdapter = DetallesOrdenAdapter()
        binding.recicladorDetalle.layoutManager = LinearLayoutManager(itemView.context)
        binding.recicladorDetalle.adapter = ordenDetallesAdapter
        binding.recicladorDetalle.setHasFixedSize(true)
        Timber.d("tipoEstado : %s ", orden.estado)
        ordenDetallesAdapter.actualizarLista(orden.ordenDetallesList)
        Timber.d("setOnClickListener:%s ", orden.status)
        validarStatus(orden)
        with(binding.root) {
            setOnClickListener {
                validarStatus(orden)
            }
            setOnLongClickListener {
                clickOnLongAnular(orden)
                return@setOnLongClickListener true
            }
        }

    }

    private fun validarStatus(orden: OrdenUi) {
        if (orden.status) {
            orden.status = false
            mostrarLista()
        } else {
            orden.status = true
            ocultarLista()
        }
    }

    private fun mostrarLista() {
        binding.recicladorDetalle.visibility = View.VISIBLE
    }

    private fun ocultarLista() {
        binding.recicladorDetalle.visibility = View.GONE
    }

}
