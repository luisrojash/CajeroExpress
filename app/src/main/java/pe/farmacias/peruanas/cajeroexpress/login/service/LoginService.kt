package pe.farmacias.peruanas.cajeroexpress.login.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path


interface LoginService {

    @GET("v1/producto/page/1/rows/{rows}")
    suspend fun obtenerListaProductos(@Path("rows") rows: String): Response<LoginResponse.LoginProductosResponse>

    @GET("v1/categoria/page/1/rows/5")
    suspend fun obtenerListaCategorias(): Response<LoginResponse.LoginCategoriasResponse>

}