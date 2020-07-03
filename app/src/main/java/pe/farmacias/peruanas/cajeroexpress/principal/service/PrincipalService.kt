package pe.farmacias.peruanas.cajeroexpress.principal.service

import pe.farmacias.peruanas.cajeroexpress.principal.productos.service.ProductosResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface PrincipalService {

    @GET("/v1/productobyname/name/{name}/page/1/rows/5")
    suspend fun obtenerProductos(@Path("name") textBuscar: String): ProductosResponse
}