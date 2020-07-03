package pe.farmacias.peruanas.cajeroexpress.prodcat.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import pe.farmacias.peruanas.cajeroexpress.R
import pe.farmacias.peruanas.cajeroexpress.databinding.ItemProdCatBinding
import pe.farmacias.peruanas.cajeroexpress.principal.productos.model.ProductosUi
import pe.farmacias.peruanas.cajeroexpress.prodcat.adapter.holder.ProdCatHolder
import timber.log.Timber


class ProdCatAdapter(
    private val clickAgregarCarrito: (ProductosUi) -> Unit,
    private val clickMostrarDetalles: (ProductosUi) -> Unit
) : RecyclerView.Adapter<ProdCatHolder>() {

    private var productoList = mutableListOf<ProductosUi>()
    private var countriesCopy = mutableListOf<ProductosUi>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdCatHolder {
        val binding = DataBindingUtil.inflate<ItemProdCatBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_prod_cat, parent, false
        )
        return ProdCatHolder(binding)
    }

    override fun getItemCount(): Int {
        return productoList.size
    }

    override fun onBindViewHolder(holder: ProdCatHolder, position: Int) {
        val producto = productoList.get(position)
        holder.bind(producto, clickAgregarCarrito, clickMostrarDetalles)
    }

    fun actualizarLista(listaProductos: List<ProductosUi>) {
        this.productoList.clear()
        this.productoList.addAll(listaProductos)
        notifyDataSetChanged()
    }

    fun filter(sequence: CharSequence) {
        val temp = mutableListOf<ProductosUi>()
        Timber.d("filter : %s ", sequence.toString())
        if (!TextUtils.isEmpty(sequence)) {
            for (s in productoList) {
                if (s.descCorta?.toLowerCase()?.contains(sequence)!! ||
                    s.nombre?.toLowerCase()?.contains(sequence)!!
                ) {
                    temp.add(s)
                }
            }
        } else {
            temp.addAll(countriesCopy)
        }
        productoList.clear()
        productoList.addAll(temp)
        notifyDataSetChanged()
        temp.clear()
    }


}

