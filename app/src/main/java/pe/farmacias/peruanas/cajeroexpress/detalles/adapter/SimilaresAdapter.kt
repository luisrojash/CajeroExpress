package pe.farmacias.peruanas.cajeroexpress.detalles.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import pe.farmacias.peruanas.cajeroexpress.R
import pe.farmacias.peruanas.cajeroexpress.databinding.ItemSimilaresBinding
import pe.farmacias.peruanas.cajeroexpress.detalles.adapter.holder.SimilaresHolder
import pe.farmacias.peruanas.cajeroexpress.detalles.ui.SimilaresUi

class SimilaresAdapter : RecyclerView.Adapter<SimilaresHolder>() {

    private val similaresUiList = mutableListOf<SimilaresUi>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimilaresHolder {
        val binding = DataBindingUtil.inflate<ItemSimilaresBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_similares, parent, false
        )
        return SimilaresHolder(binding)
    }

    override fun getItemCount(): Int {
        return similaresUiList.size
    }

    override fun onBindViewHolder(holder: SimilaresHolder, position: Int) {
        val similares = similaresUiList.get(position)
        holder.bind(similares)
    }


    fun actualizarLista(similaresUiList: List<SimilaresUi>) {
        this.similaresUiList.clear()
        this.similaresUiList.addAll(similaresUiList)
        notifyDataSetChanged()
    }
}