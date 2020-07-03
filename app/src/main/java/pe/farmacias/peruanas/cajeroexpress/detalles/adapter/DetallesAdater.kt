package pe.farmacias.peruanas.cajeroexpress.detalles.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.smarteist.autoimageslider.SliderViewAdapter
import pe.farmacias.peruanas.cajeroexpress.R
import pe.farmacias.peruanas.cajeroexpress.databinding.ItemDetallesBinding
import pe.farmacias.peruanas.cajeroexpress.detalles.adapter.holder.DetallesHolder
import pe.farmacias.peruanas.cajeroexpress.detalles.ui.DetallesUi

class DetallesAdater : SliderViewAdapter<DetallesHolder>() {

    private var detallestList = mutableListOf<DetallesUi>()

    override fun onCreateViewHolder(parent: ViewGroup?): DetallesHolder {
        val binding = DataBindingUtil.inflate<ItemDetallesBinding>(
            LayoutInflater.from(parent?.context),
            R.layout.item_detalles, parent, false
        )
        return DetallesHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: DetallesHolder?, position: Int) {
        val detalles = detallestList.get(position)
        viewHolder?.bind(detalles)
    }

    override fun getCount(): Int {
        return detallestList.size
    }

    fun setMostrarLista(detallesList: List<DetallesUi>) {
        this.detallestList.clear()
        this.detallestList.addAll(detallesList)
        notifyDataSetChanged()
    }
}