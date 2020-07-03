package pe.farmacias.peruanas.cajeroexpress.intro.local.remote

import pe.farmacias.peruanas.cajeroexpress.base.BaseDataSource
import pe.farmacias.peruanas.cajeroexpress.intro.local.service.LocalService
import javax.inject.Inject


class LocalRemote @Inject constructor(private val service: LocalService) : BaseDataSource() {

    suspend fun obtenerListaProductos(codigoLoca: String) =
        getResult { service.obtenerListaProductos(codigoLoca) }


}