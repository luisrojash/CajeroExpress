package pe.farmacias.peruanas.cajeroexpress.orden.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import pe.farmacias.peruanas.cajeroexpress.R
import pe.farmacias.peruanas.cajeroexpress.dao.CarritoResp
import pe.farmacias.peruanas.cajeroexpress.databinding.OrdenDetalleBinding
import pe.farmacias.peruanas.cajeroexpress.orden.adapter.holder.DetallesOrdenHolder

class DetallesOrdenAdapter : RecyclerView.Adapter<DetallesOrdenHolder>() {

    private var detalleList = mutableListOf<CarritoResp.ObtenerOrdenDetalle>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetallesOrdenHolder {
        val binding = DataBindingUtil.inflate<OrdenDetalleBinding>(
            LayoutInflater.from(parent.context),
            R.layout.orden_detalle, parent, false
        )
        return DetallesOrdenHolder(binding)
    }

    override fun onBindViewHolder(holder: DetallesOrdenHolder, position: Int) {
        val orden = detalleList.get(position)
        holder.bind(orden)
    }

    override fun getItemCount(): Int {
        return detalleList.size
    }

    fun actualizarLista(categoriasList: List<CarritoResp.ObtenerOrdenDetalle>) {
        this.detalleList.clear()
        this.detalleList.addAll(categoriasList)
        notifyDataSetChanged()
    }
}