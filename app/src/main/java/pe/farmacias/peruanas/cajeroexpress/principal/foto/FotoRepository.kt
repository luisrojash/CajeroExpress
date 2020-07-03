package pe.farmacias.peruanas.cajeroexpress.principal.foto

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import pe.farmacias.peruanas.cajeroexpress.base.Resource
import pe.farmacias.peruanas.cajeroexpress.base.ResponseHandler
import pe.farmacias.peruanas.cajeroexpress.db.CajeroDB
import pe.farmacias.peruanas.cajeroexpress.model.Carrito
import pe.farmacias.peruanas.cajeroexpress.model.Productos
import pe.farmacias.peruanas.cajeroexpress.principal.foto.service.FotoResponse
import pe.farmacias.peruanas.cajeroexpress.principal.foto.service.FotoService
import pe.farmacias.peruanas.cajeroexpress.principal.productos.model.ProductosUi
import timber.log.Timber
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FotoRepository @Inject constructor(
    private val service: FotoService,
    private val responseHandler: ResponseHandler,
    private val db: CajeroDB
) {
    /*suspend fun obtenerProducto(codigoProducto: String): Resource<FotoResponse> {
        return try {
            val response = service.obtenerProducto(codigoProducto)
            Timber.d("repository: %s ", codigoProducto)
            return responseHandler.handleSuccess(response)
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }*/

    fun obtenerProducto(codigoProducto: String): LiveData<Resource<List<ProductosUi>>> {
        val data = MediatorLiveData<Resource<List<ProductosUi>>>()
        data.value = Resource.loading(null)
        Timber.d("codigoProducto: %s ", codigoProducto)
        data.addSource(db.productosDao().obtenerProducto(codigoProducto)) {
            if (it != null) {
                data.value = Resource.success(obtenerProductoUi(it))
            }
        }
        return data
    }

    private fun obtenerProductoUi(it: List<Productos>): List<ProductosUi>? {
        val listProductosUi: MutableList<ProductosUi> = ArrayList()
        for (productos in it) {
            Timber.d("productos : %s ", productos.nombre)
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
        return listProductosUi
    }

    fun agregarCarrito(productosUi: ProductosUi): Resource<String> {
        try {
            val validarExisteProducto =
                db.carritoDao().validarExisteProducto(productosUi.productoId)
            Timber.d("validarExisteProducto: %s", validarExisteProducto)

            if (validarExisteProducto == 0) {
                /*No existe un Producto - Agregamos*/

                Timber.d("totalCantidadProducto: %S ", productosUi.cantidadProducto)
                Timber.d("precioTotal: %s", productosUi.precio)

                val cantidadProducto = productosUi.cantidadProducto

                val resultadoPreciototal =
                    productosUi.precio!!.toDouble() * cantidadProducto.toDouble()
                val decimal = BigDecimal(resultadoPreciototal).setScale(2, RoundingMode.HALF_EVEN)

                db.carritoDao().guardarCarrito(
                    Carrito(
                        productosUi.productoId,
                        productosUi.nombre,
                        productosUi.cantidadProducto.toInt(),
                        productosUi.imageM,
                        productosUi.precio,
                        decimal.toString(),
                        productosUi.descCorta!!
                    )
                )
                return Resource.success("Guardo Correctamente")
            } else {
                /*Actualizamos*/
                val carrito = db.carritoDao().obtenerdatosCarrito(productosUi.productoId)


                val totalCantidad = carrito.cantidadProducto + productosUi.cantidadProducto.toInt()

                Timber.d("totalCantidad: %s ", totalCantidad)

                val actualizarProducto = db.carritoDao().actualizarCantidadProducto(
                    productosUi.productoId,
                    totalCantidad
                )
                if (actualizarProducto == -1) {
                    return Resource.success("actualizarProducto Error")
                } else {
                    val carrito = db.carritoDao().obtenerdatosCarrito(productosUi.productoId)

                    val cantidadProducto = carrito.cantidadProducto
                    val totalPrecioProducto = carrito.precioTotalProducto
                    val precioProducto = carrito.precioProducto

                    val resultadoPreciototal = precioProducto.toDouble() * cantidadProducto
                    val decimal =
                        BigDecimal(resultadoPreciototal).setScale(2, RoundingMode.HALF_EVEN)

                    val actualizarTotalProductos = db.carritoDao().actualizarPrecioTotal(
                        productosUi.productoId,
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