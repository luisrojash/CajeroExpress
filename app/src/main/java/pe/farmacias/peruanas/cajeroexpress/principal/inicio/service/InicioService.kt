package pe.farmacias.peruanas.cajeroexpress.principal.inicio.service

import pe.farmacias.peruanas.cajeroexpress.principal.categorias.service.CategoriasResponse
import retrofit2.http.GET

interface InicioService {

    @GET("/v1/prodpopular/page/1/rows/5")
    suspend fun obtenerTopProductos(): InicioResponse

    @GET("/v1/categoria/page/1/rows/5")
    suspend fun obtenerListaCategorias(): CategoriasResponse
}