package pe.farmacias.peruanas.cajeroexpress.prodcat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import pe.farmacias.peruanas.cajeroexpress.base.Resource
import pe.farmacias.peruanas.cajeroexpress.base.ResponseHandler
import pe.farmacias.peruanas.cajeroexpress.db.CajeroDB
import pe.farmacias.peruanas.cajeroexpress.model.Carrito
import pe.farmacias.peruanas.cajeroexpress.model.Productos
import pe.farmacias.peruanas.cajeroexpress.principal.productos.model.ProductosUi
import pe.farmacias.peruanas.cajeroexpress.prodcat.service.ProdCatResponse
import pe.farmacias.peruanas.cajeroexpress.prodcat.service.ProdCatService
import timber.log.Timber
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProdCatRepository @Inject constructor(
    private val service: ProdCatService,
    private val responseHandler: ResponseHandler,
    private val db: CajeroDB
) {

    /* suspend fun obtenerListaCategoriasProd(idCategoria: String): Resource<ProdCatResponse> {
         return try {
             val response = service.obtenerProductos(idCategoria)
             return responseHandler.handleSuccess(response)
         } catch (e: Exception) {
             responseHandler.handleException(e)
         }
     }*/
    fun obtenerListaCategoriasProd(idCategoria: String): LiveData<Resource<List<ProductosUi>>> {
        val data = MediatorLiveData<Resource<List<ProductosUi>>>()
        data.value = Resource.loading(null)
        data.addSource(db.productosDao().obtenerListaProductosCatPrimera(idCategoria)) {
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

    fun agregarCarrito(productos: ProductosUi): Resource<String> {
        try {
            val validarExisteProducto = db.carritoDao().validarExisteProducto(productos.productoId)
            Timber.d("validarExisteProducto: %s", validarExisteProducto)
            if (validarExisteProducto == 0) {
                /*No existe un Producto - Agregamos*/
                db.carritoDao().guardarCarrito(
                    Carrito(
                        productos.productoId,
                        productos.descCorta!!,
                        1,
                        productos.imageL!!,
                        productos.precio!!,
                        productos.precio,
                        productos.descCorta
                    )
                )
                return Resource.success("Guardo Correctamente")
            } else {
                /*Actualizamos*/
                val carrito = db.carritoDao().obtenerdatosCarrito(productos.productoId)
                Timber.d("carrito: %s ", carrito.nombreProducto)
                val totalCantidad = carrito.cantidadProducto.plus(1)
                Timber.d("totalCantidad: %s ", totalCantidad)

                val actualizarProducto = db.carritoDao().actualizarCantidadProducto(
                    productos.productoId,
                    totalCantidad
                )
                if (actualizarProducto == -1) {
                    return Resource.success("actualizarProducto Error")
                } else {

                    val carrito = db.carritoDao().obtenerdatosCarrito(productos.productoId)

                    val cantidadProducto = carrito.cantidadProducto
                    val totalPrecioProducto = carrito.precioTotalProducto
                    val precioProducto = carrito.precioProducto

                    Timber.d("cantidadProducto: %s ", cantidadProducto)
                    Timber.d("totalPrecioProducto: %s ", totalPrecioProducto)
                    Timber.d("precioProducto: %s ", precioProducto)

                    val resultadoPreciototal = precioProducto.toDouble() * cantidadProducto
                    val decimal = BigDecimal(resultadoPreciototal).setScale(2, RoundingMode.HALF_EVEN)
                    Timber.d("total: %s ", totalPrecioProducto)

                    val actualizarTotalProductos = db.carritoDao().actualizarPrecioTotal(
                        productos.productoId,
                        decimal.toString()
                    )


                    if (actualizarTotalProductos == -1) {
                        return Resource.success("actualizarTotalProductos Error")
                    } else {
                        return Resource.success("Actualizo Correctamente")
                    }


                }
            }
        } catch (e: Exception) {
            return Resource.error(e.localizedMessage, e.message)
        }
    }

}