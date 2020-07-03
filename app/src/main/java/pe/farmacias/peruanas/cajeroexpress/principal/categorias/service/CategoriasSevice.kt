package pe.farmacias.peruanas.cajeroexpress.principal.categorias.service

import retrofit2.http.GET

interface CategoriasSevice {

    @GET("/v1/categoria/page/1/rows/5")
    suspend fun obtenerListaCategorias(): CategoriasResponse
}