package pe.farmacias.peruanas.cajeroexpress.carrito.order

import pe.farmacias.peruanas.cajeroexpress.base.Resource
import pe.farmacias.peruanas.cajeroexpress.db.CajeroDB
import pe.farmacias.peruanas.cajeroexpress.model.Orden
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepository @Inject constructor(private val db: CajeroDB) {


    fun guardarOrden(it: Orden): Resource<String> {
        try {
            val resp = db.ordenDao().guardarOrden(it)
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