package pe.farmacias.peruanas.cajeroexpress.principal.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.annotation.LayoutRes
import pe.farmacias.peruanas.cajeroexpress.principal.productos.model.ProductosUi


class BuscadorProducto(context: Context, @LayoutRes private val layoutResource: Int, private val allPois: MutableList<ProductosUi>):
    ArrayAdapter<ProductosUi>(context, layoutResource, allPois),
    Filterable {
    private var mPois: MutableList<ProductosUi> = allPois

    override fun getCount(): Int {
        return mPois.size
    }

    override fun getItem(p0: Int): ProductosUi? {
        return mPois.get(p0)

    }
    override fun getItemId(p0: Int): Long {
        // Or just return p0
        return mPois.get(p0).id
    }


     override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
         val view: TextView = convertView as TextView? ?: LayoutInflater.from(context).inflate(layoutResource, parent, false) as TextView
         view.text = mPois[position].nombre
         return view
     }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun publishResults(charSequence: CharSequence?, filterResults: Filter.FilterResults) {
                mPois = filterResults.values as MutableList<ProductosUi>
                notifyDataSetChanged()
            }

            override fun performFiltering(charSequence: CharSequence?): Filter.FilterResults {
                val queryString = charSequence?.toString()?.toLowerCase()

                val filterResults = Filter.FilterResults()
                filterResults.values = if (queryString==null || queryString.isEmpty())
                    allPois
                else
                    allPois.filter {
                        it.nombre.toLowerCase().contains(queryString)
                    }

                return filterResults
            }

        }
    }
     fun actualizarLista(listaProductoResp: List<ProductosUi>) {
         this.mPois.clear()
         mPois.addAll(listaProductoResp)
         notifyDataSetChanged()
     }
}





