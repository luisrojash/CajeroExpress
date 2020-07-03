package pe.farmacias.peruanas.cajeroexpress.carrito.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import pe.farmacias.peruanas.cajeroexpress.R
import pe.farmacias.peruanas.cajeroexpress.carrito.adapter.holder.CarritoHolder
import pe.farmacias.peruanas.cajeroexpress.databinding.ItemCarritoBinding
import pe.farmacias.peruanas.cajeroexpress.model.Carrito
import timber.log.Timber

class CarritoAdapter(
    private val clickSumarListener: (Carrito) -> Unit,
    private val clickRestaListener: (Carrito) -> Unit,
    private val clickEliminarListener: (Carrito) -> Unit
) : RecyclerView.Adapter<CarritoHolder>() {

    private var carritoList = mutableListOf<Carrito>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarritoHolder {
        val binding = DataBindingUtil.inflate<ItemCarritoBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_carrito, parent, false
        )
        return CarritoHolder(binding)
    }

    override fun getItemCount(): Int {
        return carritoList.size
    }

    override fun onBindViewHolder(holder: CarritoHolder, position: Int) {
        val carrito = carritoList.get(position)
        holder.bind(
            carrito,
            clickSumarListener,
            clickRestaListener,
            clickEliminarListener
        )
    }

    fun actualizarLista(carritoList: List<Carrito>) {
        Timber.d("adapter: %s ", carritoList.size)
        this.carritoList.clear()
        this.carritoList.addAll(carritoList)
        notifyDataSetChanged()
    }
}