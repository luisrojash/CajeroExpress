package pe.farmacias.peruanas.cajeroexpress.detalles

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import pe.farmacias.peruanas.cajeroexpress.base.Resource
import pe.farmacias.peruanas.cajeroexpress.base.ResponseHandler
import pe.farmacias.peruanas.cajeroexpress.db.CajeroDB
import pe.farmacias.peruanas.cajeroexpress.detalles.service.DetallesResponse
import pe.farmacias.peruanas.cajeroexpress.detalles.service.DetallesService
import pe.farmacias.peruanas.cajeroexpress.detalles.ui.DetallesUi
import pe.farmacias.peruanas.cajeroexpress.detalles.ui.SimilaresUi
import pe.farmacias.peruanas.cajeroexpress.model.Carrito
import pe.farmacias.peruanas.cajeroexpress.model.Productos
import timber.log.Timber
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DetallesRepository @Inject constructor(
    private val service: DetallesService,
    private val responseHandler: ResponseHandler,
    private val db: CajeroDB
) {
    /*suspend fun obtenerListaProductosSimilar(): Resource<DetallesResponse> {
        return try {
            val response = service.obtenerListaSimilares()
            return responseHandler.handleSuccess(response)
        } catch (e: Exception) {
            responseHandler.handleException(e)

        }
    }*/

    fun obtenerListaProductosSimilar(nombre: String): LiveData<Resource<List<SimilaresUi>>> {
        val data = MediatorLiveData<Resource<List<SimilaresUi>>>()
        data.value = Resource.loading(null)
        val separate1 = nombre.split(" ")[0]
        Timber.d("separate1 : %s ", separate1)
        data.addSource(db.productosDao().obtenerListaProductoBuscarSimilar(separate1)) {
            if (it != null) {
                data.value = Resource.success(obtenerListaProductosUi(it))
            }
        }
        return data
    }

    private fun obtenerListaProductosUi(it: List<Productos>?): List<SimilaresUi>? {
        val similaresUi: MutableList<SimilaresUi> = ArrayList()
        if (it != null) {
            for (productos in it) {
                similaresUi.add(
                    SimilaresUi(
                        productos.id,
                        productos.productoId,
                        productos.nombre,
                        productos.descCorta,
                        productos.descLarga.toString(),
                        productos.precio,
                        "",
                        "",
                        productos.imageL,
                        productos.imageM,
                        productos.imageX,
                        productos.imageX
                    )
                )
            }
        }
        return similaresUi
    }


    fun agregarCarrito(cantidadProductos: String, detallesUi: DetallesUi): Resource<String> {
        try {
            val validarExisteProducto =
                db.carritoDao().validarExisteProducto(detallesUi.productoId.toString())
            Timber.d("validarExisteProducto: %s", validarExisteProducto)
            if (validarExisteProducto == 0) {
                /*No existe un Producto - Agregamos*/
                Timber.d("guardarCarrito")
                val resultadoPreciototal =
                    detallesUi.precio!!.toDouble() * cantidadProductos.toDouble()
                val decimal = BigDecimal(resultadoPreciototal).setScale(2, RoundingMode.HALF_EVEN)

                db.carritoDao().guardarCarrito(
                    Carrito(
                        detallesUi.productoId.toString(),
                        detallesUi.nombre.toString(),
                        cantidadProductos.toInt(),
                        detallesUi.imageView.toString(),
                        detallesUi.precio.toString(),
                        decimal.toString(),
                        detallesUi.descCorta.toString()
                    )
                )
                return Resource.success("Guardo Correctamente")
            } else {
                /*Actualizamos*/
                val carrito = db.carritoDao().obtenerdatosCarrito(detallesUi.productoId.toString())


                val totalCantidad = carrito.cantidadProducto + cantidadProductos.toInt()
                Timber.d("totalCantidad: %s ", totalCantidad)


                val actualizarProducto = db.carritoDao().actualizarCantidadProducto(
                    detallesUi.productoId,
                    totalCantidad
                )
                if (actualizarProducto == -1) {
                    return Resource.success("actualizarProducto Error")
                } else {
                    val resultadoPreciototal = detallesUi.precio!!.toDouble() * totalCantidad
                    val decimal =
                        BigDecimal(resultadoPreciototal).setScale(2, RoundingMode.HALF_EVEN)

                    val actualizarTotalProductos = db.carritoDao().actualizarPrecioTotal(
                        detallesUi.productoId,
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