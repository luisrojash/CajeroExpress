package pe.farmacias.peruanas.cajeroexpress.principal.foto.service

import retrofit2.http.GET
import retrofit2.http.Path

interface FotoService {

    @GET("/v1/productobyCodigoProducto/codProducto/{codProducto}/page/1/rows/5")
    suspend fun obtenerProducto(@Path("codProducto") codProducto: String): FotoResponse
}