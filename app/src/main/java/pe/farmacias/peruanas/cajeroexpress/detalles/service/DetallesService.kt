package pe.farmacias.peruanas.cajeroexpress.detalles.service

import retrofit2.http.GET

interface DetallesService {

    @GET("/v1/prodsimilar/page/1/rows/5")
    suspend fun obtenerListaSimilares(): DetallesResponse

}