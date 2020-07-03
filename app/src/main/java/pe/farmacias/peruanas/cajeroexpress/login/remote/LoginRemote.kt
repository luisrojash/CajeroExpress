package pe.farmacias.peruanas.cajeroexpress.login.remote

import pe.farmacias.peruanas.cajeroexpress.base.BaseDataSource
import pe.farmacias.peruanas.cajeroexpress.login.service.LoginService
import javax.inject.Inject

class LoginRemote @Inject constructor(private val service: LoginService) : BaseDataSource() {

    suspend fun obtenerListaProductos() = getResult { service.obtenerListaProductos("6000") }

    suspend fun obtenerListCategorias() = getResult { service.obtenerListaCategorias() }
}