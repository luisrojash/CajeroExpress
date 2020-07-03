package pe.farmacias.peruanas.cajeroexpress.carrito

import android.database.sqlite.SQLiteException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pe.farmacias.peruanas.cajeroexpress.base.Resource
import pe.farmacias.peruanas.cajeroexpress.base.ResponseHandler
import pe.farmacias.peruanas.cajeroexpress.carrito.service.CarritoService
import pe.farmacias.peruanas.cajeroexpress.db.CajeroDB
import pe.farmacias.peruanas.cajeroexpress.model.Carrito
import pe.farmacias.peruanas.cajeroexpress.model.Orden
import timber.log.Timber
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject


class CarritoRepository @Inject constructor(
    private val db: CajeroDB,
    private val responseHandler: ResponseHandler,
    private val services: CarritoService
) {

    fun agregarSumaProducto(it: Carrito, totalCantidad: Int, resultado: MutableLiveData<String>) {
        GlobalScope.launch(Dispatchers.IO) {
            val response = db.carritoDao().actualizarCantidadProducto(
                it.codigoProducto,
                totalCantidad.toString().toInt()
            )
            Timber.d("agregarSumaProducto : %s ", response.toString())
            if (response.toString().equals("-1")) {
                resultado.postValue("ERROR ACTUALIZAR SUMA CARRITO")
            } else {

                try {
                    val carritoresp =
                        db.carritoDao().obtenerDatosProductoSqlite(it.codigoProducto)

                    val resultadoPreciototal = carritoresp.precioProducto.toDouble() *
                            carritoresp.cantidadProducto.toDouble()

                    val decimal =
                        BigDecimal(resultadoPreciototal).setScale(2, RoundingMode.HALF_EVEN)
                    /*actualizamos el precio total del producto*/
                    val resultadoTotalSoles = db.carritoDao().actualizarPrecioTotal(
                        it.codigoProducto,
                        decimal.toString()
                    )
                    if (resultadoTotalSoles == -1) {
                        resultado.postValue("ERROR ACTUALIZAR TOTAL PRODUCTOS")
                    } else {
                        resultado.postValue("COMPLETADO")
                    }

                    Timber.d(
                        "carritoresp.cantidadProducto.toDouble() : %s ",
                        carritoresp.cantidadProducto.toDouble()
                    )
                    Timber.d(
                        " carritoresp.precioProducto.toDouble() : %s ",
                        carritoresp.precioProducto.toDouble()
                    )
                    Timber.d("resultadoPreciototal : %s ", resultadoPreciototal)


                } catch (exeption: Exception) {
                    resultado.postValue("ERROR OBTENER DATOS")
                }
            }
        }
    }

    fun restarProducto(it: Carrito, totalCantidad: Int, resultado: MutableLiveData<String>) {

        val oldPrecioTotalProducto = it.precioTotalProducto
        val oldPrecioProducto = it.precioProducto
        GlobalScope.launch(Dispatchers.IO) {
            val response = db.carritoDao()
                .actualizarCantidadProducto(it.codigoProducto, totalCantidad)

            if (response.toString().equals("-1")) {
                resultado.postValue("ERROR ACTUALIZAR RESTA CARRITO")
            } else {
                try {
                    val resultadoPreciototal = oldPrecioTotalProducto!!.toDouble() -
                            oldPrecioProducto!!.toDouble()

                    val decimal =
                        BigDecimal(resultadoPreciototal).setScale(2, RoundingMode.HALF_EVEN)

                    val resultadoTotalSoles = db.carritoDao().actualizarPrecioTotal(
                        it.codigoProducto,
                        decimal.toString()
                    )
                    if (resultadoTotalSoles.toString().equals("-1")) {
                        resultado.postValue("ERROR ACTUALIZAR TOTAL PRODUCTOS")
                    } else {
                        resultado.postValue("COMPLETADO")
                    }


                } catch (exeption: SQLiteException) {
                    resultado.postValue("ERROR OBTENER DATOS")
                }
            }
        }
    }

    fun eliminarProductoCarrito(it: Carrito): Resource<String> {
        return try {
            Timber.d("idCarrito : %s ", it.idCarrito)
            val resp = db.carritoDao().eliminarCarrito(it.idCarrito.toString())
            Timber.d("resp : %s ", resp)
            if (resp == -1) {
                Resource.error("ERROR AL ELIMINAR")
            } else {
                Resource.success("GUARDO CORRECTAMENTE")
            }
        } catch (e: Exception) {
            Resource.error(e.localizedMessage, e.message)
        }
    }

    fun obtenerListaCarrito(): LiveData<Resource<List<Carrito>>> {
        val data = MediatorLiveData<Resource<List<Carrito>>>()
        data.value = Resource.loading(null)
        data.addSource(db.carritoDao().obtenerListaCarrito()) {
            if (it != null) {
                data.value = Resource.success(it)
            }
        }
        return data
    }

    fun guardarOrdenCarrito(ordenList: List<Orden>?): Resource<String> {
        try {
            val resp = db.ordenDao().guardarOrdenList(ordenList!!)
            Timber.d("resp: %s ", resp)
            val limpiarCarrito = db.carritoDao().limpiarCarrito()
            Timber.d("resp : %s ", resp)
            if (limpiarCarrito == -1) {
                return Resource.error("ERROR AL ELIMINAR")
            } else {
                return Resource.success("GUARDO CORRECTAMENTE")
            }
        } catch (e: Exception) {
            return Resource.error(e.localizedMessage, e.message)
        }
    }

}