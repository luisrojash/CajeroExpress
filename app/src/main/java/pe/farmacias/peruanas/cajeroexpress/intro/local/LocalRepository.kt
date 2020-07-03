package pe.farmacias.peruanas.cajeroexpress.intro.local

import androidx.lifecycle.distinctUntilChanged
import pe.farmacias.peruanas.cajeroexpress.base.Resource
import pe.farmacias.peruanas.cajeroexpress.base.ResponseHandler
import pe.farmacias.peruanas.cajeroexpress.base.resultLiveData
import pe.farmacias.peruanas.cajeroexpress.db.CajeroDB
import pe.farmacias.peruanas.cajeroexpress.intro.local.remote.LocalRemote
import pe.farmacias.peruanas.cajeroexpress.intro.local.service.LocalResponse
import pe.farmacias.peruanas.cajeroexpress.intro.local.service.LocalService
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalRepository @Inject constructor(
    private val db: CajeroDB,
    //private val remote: LocalRemote,
    private val responseHandler: ResponseHandler,
    private val service: LocalService
) {
   /* fun obtenerProductos(id: String) = resultLiveData(
        databaseQuery = { db.productosDao().obtenerListaProductos() },
        networkCall = { remote.obtenerListaProductos(id) },
        saveCallResult = { db.productosDao().insertarProductos(it.productoLista) })*/

    suspend fun obtenerProductos(codigoLocal: String): Resource<Response<LocalResponse.LocalResponseList>> {
        return try {
            val response = service.obtenerListaProductos(codigoLocal)
            return responseHandler.handleSuccess(response)
        } catch (e: Exception) {
            responseHandler.handleException(e)

        }
    }

}

