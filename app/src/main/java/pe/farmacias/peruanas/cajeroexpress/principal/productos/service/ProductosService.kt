package pe.farmacias.peruanas.cajeroexpress.principal.productos.service

import retrofit2.Response
import retrofit2.http.GET

interface ProductosService {

    @GET("v1/producto/page/1/rows/5")
    suspend fun obtenerProductos2(): Response<ProductosResponse>

    @GET("v1/producto/page/1/rows/5")
    suspend fun obtenerProductos(): ProductosResponse

}