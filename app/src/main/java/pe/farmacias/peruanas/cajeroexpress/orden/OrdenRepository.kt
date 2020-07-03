package pe.farmacias.peruanas.cajeroexpress.orden

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pe.farmacias.peruanas.cajeroexpress.base.Resource
import pe.farmacias.peruanas.cajeroexpress.dao.CarritoResp
import pe.farmacias.peruanas.cajeroexpress.db.CajeroDB
import pe.farmacias.peruanas.cajeroexpress.model.Orden
import pe.farmacias.peruanas.cajeroexpress.orden.model.OrdenUi
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrdenRepository @Inject constructor(
    private val db: CajeroDB
) {


    fun obtenerListaCarrito(): LiveData<Resource<List<OrdenUi>>> {
        val data = MediatorLiveData<Resource<List<OrdenUi>>>()
        data.value = Resource.loading(null)
        data.addSource(db.ordenDao().obtenerListaOrden()) {
            if (it != null) {
                data.value = Resource.success(obtenerDetallesOrden(it))
            }
        }
        return data
    }

    private fun obtenerDetallesOrden(it: List<Orden>?): List<OrdenUi>? {
        val ordenList: MutableList<OrdenUi> = ArrayList()
        var conteo = 0
        if (it != null) {
            for (orderIt in it) {
                conteo++
                val orden = OrdenUi(
                    orderIt.fecha,
                    orderIt.nombreUsuario,
                    orderIt.nombreLocal,
                    orderIt.totalSoles,
                    orderIt.totalCantidad,
                    orderIt.productoId,
                    orderIt.referenciaId,
                    orderIt.estado,
                    conteo.toString(),
                    false
                )
                GlobalScope.launch(Dispatchers.IO) {
                    val listaDetalles =
                        db.ordenDao().obtenerListaOrdenDetalles(orderIt.referenciaId)
                    Timber.d("listaDetalles: %S ", listaDetalles.size)
                    val detallesOrdenList: MutableList<CarritoResp.ObtenerOrdenDetalle> =
                        ArrayList()
                    for (detalles in listaDetalles) {
                        val detalleOrden = CarritoResp.ObtenerOrdenDetalle(
                            detalles.nombreProducto,
                            detalles.descripcionCortaProducto,
                            detalles.precioProducto,
                            detalles.productosImagen
                        )
                        detallesOrdenList.add(detalleOrden)
                        orden.ordenDetallesList = detallesOrdenList
                    }
                }
                ordenList.add(orden)
            }
        }
        return ordenList
    }

    fun actualizarOrden(ordenUi: OrdenUi?): Resource<String> {
        return try {
            val resp = db.ordenDao().actualizarOrden(ordenUi!!.referenciaId, "Anulado")
            Timber.d("resp : %s ", resp)
            if (resp == -1) {
                Resource.error("ERROR AL ACTUALIZAR")
            } else {
                Resource.success("GUARDO CORRECTAMENTE")
            }
        } catch (e: Exception) {
            Resource.error(e.localizedMessage, e.message)
        }
    }

}