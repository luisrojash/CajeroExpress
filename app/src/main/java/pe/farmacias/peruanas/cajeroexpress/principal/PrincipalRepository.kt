package pe.farmacias.peruanas.cajeroexpress.principal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import pe.farmacias.peruanas.cajeroexpress.base.Resource
import pe.farmacias.peruanas.cajeroexpress.base.ResponseHandler
import pe.farmacias.peruanas.cajeroexpress.db.CajeroDB
import pe.farmacias.peruanas.cajeroexpress.model.Productos
import pe.farmacias.peruanas.cajeroexpress.principal.productos.model.ProductosUi
import pe.farmacias.peruanas.cajeroexpress.principal.productos.service.ProductosResponse
import pe.farmacias.peruanas.cajeroexpress.principal.service.PrincipalService
import timber.log.R
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrincipalRepository @Inject constructor(
    private val service: PrincipalService,
    private val responseHandler: ResponseHandler,
    private val db: CajeroDB
) {



    fun obtenerListaProductos(textoBuscar: String): LiveData<Resource<List<ProductosUi>>> {
        val data = MediatorLiveData<Resource<List<ProductosUi>>>()
        data.value = Resource.loading(null)
        data.addSource(db.productosDao().obtenerListaProductoBuscar(textoBuscar)) {
            if (it != null) {
                data.value = Resource.success(obtenerListaProductosUi(it))
            }
        }
        return data
    }

    private fun obtenerListaProductosUi(it: List<Productos>?): List<ProductosUi>? {
        val listProductosUi: MutableList<ProductosUi> = ArrayList()
        if (it != null) {
            for (productos in it) {
                Timber.d("productos :%s ", productos.nombre)
                listProductosUi.add(
                    ProductosUi(
                        productos.id.toLong(),
                        productos.productoId,
                        productos.esLam,
                        productos.nombre,
                        productos.descCorta,
                        productos.descLarga,
                        productos.categoriaId,
                        productos.categoriaDesc,
                        productos.categoriaId2,
                        productos.categoriaDesc2,
                        productos.categoriaId3,
                        productos.categoriaDesc3,
                        productos.imageL,
                        productos.imageM,
                        productos.imageX,
                        productos.precio,
                        "",
                        "",
                        ""
                    )
                )
            }
        }
        return listProductosUi
    }

    fun obtenerConteoCarrito(): LiveData<Resource<String>> {
        val data = MediatorLiveData<Resource<String>>()
        data.value = Resource.loading(null)
        data.addSource(db.carritoDao().obtenerConteoCarrito()) {
            if (it != null) {
                data.value = Resource.success(it)
            }
        }
        return data
    }
}