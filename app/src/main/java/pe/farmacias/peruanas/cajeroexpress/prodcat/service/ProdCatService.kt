package pe.farmacias.peruanas.cajeroexpress.prodcat.service

import retrofit2.http.GET
import retrofit2.http.Path

interface ProdCatService {

    @GET("/v1/productobyCategoria/categoriaId/{categoriaId}/page/1/rows/5")
    suspend fun obtenerProductos(@Path("categoriaId") categoriaId: String): ProdCatResponse

}