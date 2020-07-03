package pe.farmacias.peruanas.cajeroexpress.principal.inicio

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import pe.farmacias.peruanas.cajeroexpress.base.Resource
import pe.farmacias.peruanas.cajeroexpress.base.ResponseHandler
import pe.farmacias.peruanas.cajeroexpress.db.CajeroDB
import pe.farmacias.peruanas.cajeroexpress.model.Categoria
import pe.farmacias.peruanas.cajeroexpress.model.Productos
import pe.farmacias.peruanas.cajeroexpress.principal.categorias.model.CategoriasUi
import pe.farmacias.peruanas.cajeroexpress.principal.inicio.model.TopProducto
import pe.farmacias.peruanas.cajeroexpress.principal.inicio.service.InicioService
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InicioRepository @Inject constructor(
    private val db: CajeroDB
) {

    fun obtenerListaTopProducto(): LiveData<Resource<List<TopProducto>>> {
        val data = MediatorLiveData<Resource<List<TopProducto>>>()
        data.value = Resource.loading(null)
        data.addSource(db.productosDao().obtenerListaProductos()) {
            if (it != null) {
                data.value = Resource.success(setListaProducto(it))
            }
        }
        return data
    }

    private fun setListaProducto(it: List<Productos>?): List<TopProducto>? {
        val listaProductos: MutableList<TopProducto> = ArrayList()
        if (it != null) {
            for (prod in it) {
                val productosUi = TopProducto(
                    prod.id,
                    prod.productoId,
                    prod.nombre,
                    prod.descLarga.toString(),
                    prod.precio,
                    prod.precio,//precioTachado
                    "12",
                    prod.imageL,
                    prod.imageM,
                    prod.imageX,
                    prod.descCorta
                )
                listaProductos.add(productosUi)
            }
        }
        return listaProductos
    }


    fun obtenerListaCategorias(): LiveData<Resource<List<CategoriasUi>>> {
        val data = MediatorLiveData<Resource<List<CategoriasUi>>>()
        data.value = Resource.loading(null)
        data.addSource(db.categoriasDao().obtenerListaCategoriaAll("")) {
            if (it != null) {
                data.value = Resource.success(setListaCategoria(it))
            }
        }
        return data
    }

    private fun setListaCategoria(it: List<Categoria>): MutableList<CategoriasUi> {
        val listaCategorias: MutableList<CategoriasUi> = ArrayList()
        for (cat in it) {
            Timber.d("cat : %s ",cat.categoria)
            val categoriasUi = CategoriasUi(
                cat.codCategoria,
                cat.categoria,
                cat.imagen,
                "4"
            )
            listaCategorias.add(categoriasUi)
        }
        return listaCategorias
    }

}