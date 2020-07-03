package pe.farmacias.peruanas.cajeroexpress.login

import pe.farmacias.peruanas.cajeroexpress.base.resultLiveData
import pe.farmacias.peruanas.cajeroexpress.db.CajeroDB
import pe.farmacias.peruanas.cajeroexpress.login.remote.LoginRemote
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val db: CajeroDB,
    private val remoteSource: LoginRemote
) {

    val onListaProductos = resultLiveData(
        databaseQuery = { db.productosDao().obtenerListaProductos() },
        networkCall = { remoteSource.obtenerListaProductos() },
        saveCallResult = { db.productosDao().insertarProductos(it.productoLista) })

    val onListaCategorias = resultLiveData(
        databaseQuery = { db.categoriasDao().obtenerListaCategoria() },
        networkCall = { remoteSource.obtenerListCategorias() },
        saveCallResult = { db.categoriasDao().guardarListaCategorias(it.categoriaLista) })
}